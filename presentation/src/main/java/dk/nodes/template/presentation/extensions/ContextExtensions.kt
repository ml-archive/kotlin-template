package dk.nodes.template.presentation.extensions

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Activity.hideKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}

fun View.dpToPx(dp: Int): Int {
    return context.dpToPx(dp)
}

fun Fragment.dpToPx(dp: Int): Int {
    return requireContext().dpToPx(dp)
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}