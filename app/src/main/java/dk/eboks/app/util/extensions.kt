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
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.textfield.TextInputEditText
import com.l4digital.fastscroll.FastScrollRecyclerView
import com.l4digital.fastscroll.FastScroller
import dk.eboks.app.domain.config.AppConfigImpl
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.Translation
import timber.log.Timber
import java.io.Serializable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bison on 01-07-2017.
 */
// makes viewgroups iterable (you can for the each out of them!:)

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
            item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED)
            // set once again checked value, so view will be updated
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Timber.e("Unable to get shift mode field")
    } catch (e: IllegalAccessException) {
        Timber.e("Unable to change value of shift mode")
    }
}

fun Editable?.isValidEmail(): Boolean {
    return if (this == null) return false else !TextUtils.isEmpty(toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(
        toString().trim()
    ).matches()
}

fun Editable?.isValidCpr(): Boolean {
    if (this == null) return false
    val cprLength = AppConfigImpl.currentMode.cprLength
    val cprRegex = Regex("^[0-9]*$cprLength}$")
    val text = toString().trim()
    return !TextUtils.isEmpty(text) && text.matches(cprRegex)
}

fun Editable?.isValidActivationCode(): Boolean {
    if (this == null) return false
    val cprRegex = Regex("^[a-zA-Z0-9]{8}$")
    val text = toString().trim()
    return !TextUtils.isEmpty(text) && text.matches(cprRegex)
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

// fun Channel.isStorebox(): Boolean {
//    if (this.id > 0 && this.id < 4) {
//        return true
//    }
//    return false
// }

fun Image.getWorkaroundUrl(): String {
    return "$url&type=1"
}

/**
 * Add cases to this where you want to use the standard exception to view error method
 * Memba you can always create your own custom ViewError in the interactor instead of using this
 *
 * Its just for convenience yall
 */

class ActivityStarter(private val callingActivity: Activity) {
    private var activityClass: Class<out Activity>? = null
    private var intent: Intent? = null

    fun activity(activity: Class<out Activity>) = apply {
        this.activityClass = activity
        this.intent = Intent(callingActivity, activityClass)
    }

    fun putExtra(name: String, value: Serializable): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Boolean): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Byte): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Char): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Short): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Int): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Long): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Float): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Double): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: String): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: CharSequence): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Parcelable): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun putExtra(name: String, value: Array<Parcelable>): ActivityStarter =
        apply { this.intent?.putExtra(name, value) }

    fun start() {
        callingActivity.startActivity(intent)
    }
}

@Suppress("FunctionName")
fun Activity.Starter(): ActivityStarter {
    return ActivityStarter(this)
}

fun <T : Fragment> T.putArg(name: String, value: Serializable) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putSerializable(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Boolean) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putBoolean(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Byte) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putByte(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Char) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putChar(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Short) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putShort(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Int) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putInt(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Long) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putLong(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Float) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putFloat(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Double) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putDouble(name, value) }

fun <T : Fragment> T.putArg(name: String, value: String) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putString(name, value) }

fun <T : Fragment> T.putArg(name: String, value: CharSequence) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putCharSequence(name, value) }

fun <T : Fragment> T.putArg(name: String, value: Parcelable) =
    apply { arguments.guard { arguments = Bundle() }; arguments?.putParcelable(name, value) }

fun LoginProvider.translatedName(): String {
    return when (this.id) {
        "email" -> Translation.logonmethods.mobileAccess
        "cpr" -> Translation.logonmethods.mobileAccess
        "nemid" -> Translation.logonmethods.nemId
        "idporten" -> Translation.logonmethods.idPorten
        "bankid_se" -> Translation.logonmethods.bankId
        "bankid_no" -> Translation.logonmethods.nemId
        else -> {
            this.name
        }
    }
}

fun WebView.printAndForget(context: Context) {
// Get a PrintManager instance
    val printManager: PrintManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

    // Get a print adapter instance
    val printAdapter: PrintDocumentAdapter = this.createPrintDocumentAdapter()

    // Create a print job with name and adapter instance
    val jobName = "eboks"
    val printJob: PrintJob =
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.invisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

var View.gone: Boolean
    get() = visibility == View.GONE
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }

fun TextInputEditText.onTextChanged(block: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            p0?.let { block.invoke(it.toString()) }
        }
    })
}

fun TextInputEditText.onImeActionDone(block: () -> Unit) {
    setOnEditorActionListener { _, id, _ ->
        if (id == EditorInfo.IME_ACTION_DONE) {
            block()
        }
        true
    }
}

fun Date.formatPayment() : String {
    val formatter =  SimpleDateFormat("d MMMM YYYY", Locale.getDefault())
    return "Payment due on ${formatter.format(this)}"
}
