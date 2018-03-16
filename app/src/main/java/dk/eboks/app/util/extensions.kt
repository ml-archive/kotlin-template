package dk.eboks.app.util

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.text.Editable
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import com.l4digital.fastscroll.FastScrollRecyclerView
import com.l4digital.fastscroll.FastScroller
import dk.eboks.app.domain.config.Config
import timber.log.BuildConfig
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by bison on 01-07-2017.
 */
// makes viewgroups iterable (you can for the each out of them!:)
fun ViewGroup.asSequence(): Sequence<View> = object : Sequence<View> {
    override fun iterator(): Iterator<View> = object : Iterator<View> {
        private var nextValue: View? = null
        private var done = false
        private var position: Int = 0

        override fun hasNext(): Boolean {
            if (nextValue == null && !done) {
                nextValue = getChildAt(position)
                position++
                if (nextValue == null) done = true
            }
            return nextValue != null
        }

        override fun next(): View {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val answer = nextValue
            nextValue = null
            return answer!!
        }
    }
}

val ViewGroup.views: List<View>
    get() = asSequence().toList()

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftingMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            // set once again checked value, so view will be updated
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Timber.e("Unable to get shift mode field")
    } catch (e: IllegalAccessException) {
        Timber.e("Unable to change value of shift mode")
    }

}

fun Editable.isValidEmail() : Boolean {
    return !TextUtils.isEmpty(toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(toString().trim()).matches()

}

fun Editable.isValidCpr() : Boolean {
    var cprLength = Config.currentMode.cprLength
    val cprRegex = Regex("^[0-9]{"+cprLength+"}$")
    val text = toString().trim()
    return !TextUtils.isEmpty(text) && text.matches(cprRegex)
}

fun Editable.isValidActivationCode() : Boolean {
    val cprRegex = Regex("^[a-zA-Z0-9]{8}$")
    val text = toString().trim()
    return !TextUtils.isEmpty(text) && text.matches(cprRegex)
}


inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

fun FastScrollRecyclerView.setBubbleDrawable(drawable: Drawable) {
    FastScrollRecyclerView::class.java.getDeclaredField("mFastScroller").let {
        it.isAccessible = true
        val scroller = it.get(this) as FastScroller

        FastScroller::class.java.getDeclaredField("mBubbleView").let {
            it.isAccessible = true
            val bubbleView = it.get(scroller) as View
            bubbleView.background = drawable
        }
    }
}