package dk.eboks.app.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import com.l4digital.fastscroll.FastScrollRecyclerView
import com.l4digital.fastscroll.FastScroller
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.BaseInteractor
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.io.Serializable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

fun Editable.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(toString().trim()).matches()

}

fun Editable.isValidCpr(): Boolean {
    var cprLength = Config.currentMode.cprLength
    val cprRegex = Regex("^[0-9]{$cprLength}$")
    val text = toString().trim()
    return !TextUtils.isEmpty(text) && text.matches(cprRegex)
}

fun Editable.isValidActivationCode(): Boolean {
    val cprRegex = Regex("^[a-zA-Z0-9]{8}$")
    val text = toString().trim()
    return !TextUtils.isEmpty(text) && text.matches(cprRegex)
}


inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun dpToPx(dp: Float): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
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

fun EditText.addAfterTextChangeListener(listener: ((Editable?) -> Unit)) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

//fun Channel.isStorebox(): Boolean {
//    if (this.id > 0 && this.id < 4) {
//        return true
//    }
//    return false
//}

fun Channel.getType(): String {
    if (this.id > 0 && this.id < 4) {
        return "storebox"
    }
    // TODO figure out ids and reenable ekey support later

    if (this.id >= 11 && this.id <= 13) {
        return "ekey"
    }

    return "channel"
}

fun Image.getWorkaroundUrl() : String
{
    return "$url&type=1"
}

fun Channel.areAllRequirementsVerified() : Boolean
{
    this.requirements?.let { reqs ->
        for(req in reqs)
        {
            req.verified?.let {
                if(!it)
                    return false
            }
        }
        return true
    }.guard { return true }
    return true
}


/**
 * Add cases to this where you want to use the standard exception to view error method
 * Memba you can always create your own custom ViewError in the interactor instead of using this
 *
 * Its just for convenience yall
 */

fun BaseInteractor.errorBodyToViewError(
        response: Response<*>,
        shouldClose: Boolean = false
): ViewError {
    val responseString = response.errorBody()?.string()

    //Todo add more cases like 401,402, ect ect

    return when (response.code()) {
        else -> {
            ViewError(
                    Translation.error.genericTitle,
                    Translation.error.genericMessage,
                    true,
                    shouldClose
            )
        }
    }
}


internal fun throwableToViewError(t: Throwable,
                                  shouldClose: Boolean = false,
                                  shouldDisplay: Boolean = true) : ViewError
{
    when (t) {
        is ConnectException -> return ViewError(
                title = Translation.error.noInternetTitle,
                message = Translation.error.noInternetMessage,
                shouldDisplay = shouldDisplay,
                shouldCloseView = shouldClose
        )
        is UnknownHostException -> return ViewError(
                title = Translation.error.noInternetTitle,
                message = Translation.error.noInternetMessage,
                shouldDisplay = shouldDisplay,
                shouldCloseView = shouldClose
        )
        is IOException -> return ViewError(
                title = Translation.error.genericStorageTitle,
                message = Translation.error.genericStorageMessage,
                shouldDisplay = shouldDisplay,
                shouldCloseView = shouldClose
        )
        is SocketTimeoutException -> return ViewError(
                title = Translation.error.noInternetTitle,
                message = Translation.error.noInternetMessage,
                shouldDisplay = shouldDisplay,
                shouldCloseView = shouldClose
        )
        is ServerErrorException -> {
            return ViewError(
                    title = t.error.description?.title,
                    message = t.error.description?.text,
                    shouldDisplay = shouldDisplay,
                    shouldCloseView = shouldClose
            )
        }
        else -> return ViewError(
                shouldDisplay = shouldDisplay,
                shouldCloseView = shouldClose
        )
    }
}

fun BaseInteractor.exceptionToViewError(
        t: Throwable,
        shouldClose: Boolean = false,
        shouldDisplay: Boolean = true
): ViewError {
    t.cause?.let {
        return throwableToViewError(it, shouldClose, shouldDisplay)
    }.guard {
        return throwableToViewError(t, shouldClose, shouldDisplay)
    }
    return ViewError(shouldDisplay = shouldDisplay, shouldCloseView = shouldClose)
}

class ActivityStarter(val callingActivity: Activity) {
    private var activityClass: Class<out Activity>? = null
    private var intent: Intent? = null

    fun activity(activity: Class<out Activity>) = apply {
        this.activityClass = activity
        this.intent = Intent(callingActivity, activityClass)
    }

    fun putExtra(name: String, value: Serializable): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Boolean): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Byte): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Char): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Short): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Int): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Long): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Float): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Double): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: String): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: CharSequence): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Parcelable): ActivityStarter = apply { this.intent?.putExtra(name, value) }
    fun putExtra(name: String, value: Array<Parcelable>): ActivityStarter = apply { this.intent?.putExtra(name, value) }

    fun start() {
        callingActivity.startActivity(intent)
    }
}

fun Activity.Starter(): ActivityStarter {
    return ActivityStarter(this)
}

fun Fragment.putArg(name: String, value: Serializable) = apply { arguments.guard { arguments = Bundle() }; arguments?.putSerializable(name, value) }
fun Fragment.putArg(name: String, value: Boolean) = apply { arguments.guard { arguments = Bundle() }; arguments?.putBoolean(name, value) }
fun Fragment.putArg(name: String, value: Byte) = apply { arguments.guard { arguments = Bundle() }; arguments?.putByte(name, value) }
fun Fragment.putArg(name: String, value: Char) = apply { arguments.guard { arguments = Bundle() }; arguments?.putChar(name, value) }
fun Fragment.putArg(name: String, value: Short) = apply { arguments.guard { arguments = Bundle() }; arguments?.putShort(name, value) }
fun Fragment.putArg(name: String, value: Int) = apply { arguments.guard { arguments = Bundle() }; arguments?.putInt(name, value) }
fun Fragment.putArg(name: String, value: Long) = apply { arguments.guard { arguments = Bundle() }; arguments?.putLong(name, value) }
fun Fragment.putArg(name: String, value: Float) = apply { arguments.guard { arguments = Bundle() }; arguments?.putFloat(name, value) }
fun Fragment.putArg(name: String, value: Double) = apply { arguments.guard { arguments = Bundle() }; arguments?.putDouble(name, value) }
fun Fragment.putArg(name: String, value: String) = apply { arguments.guard { arguments = Bundle() }; arguments?.putString(name, value) }
fun Fragment.putArg(name: String, value: CharSequence) = apply { arguments.guard { arguments = Bundle() }; arguments?.putCharSequence(name, value) }
fun Fragment.putArg(name: String, value: Parcelable) = apply { arguments.guard { arguments = Bundle() }; arguments?.putParcelable(name, value) }

fun LoginProvider.translatedName() : String {
    when(this.id)
    {
        "email" -> return Translation.logonmethods.mobileAccess
        "cpr" -> return Translation.logonmethods.mobileAccess
        "nemid" -> return Translation.logonmethods.nemId
        "idporten" -> return Translation.logonmethods.idPorten
        "bankid_se" -> return Translation.logonmethods.bankId
        "bankid_no" -> return Translation.logonmethods.nemId
        else -> { return this.name }
    }
}

fun WebView.printAndForget(context : Context)
{
// Get a PrintManager instance
    val printManager : PrintManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

    // Get a print adapter instance
    val printAdapter : PrintDocumentAdapter = this.createPrintDocumentAdapter()

    // Create a print job with name and adapter instance
    val jobName = "eboks"
    val printJob : PrintJob = printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
}