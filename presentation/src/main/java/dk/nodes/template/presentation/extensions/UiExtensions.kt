package dk.nodes.template.presentation.extensions

import android.content.res.Resources
import kotlin.math.roundToInt

// Dp to pix or vice versa
val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)
val Float.px: Float
    get() = (this / Resources.getSystem().displayMetrics.density)


// Dp to pix or vice versa
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()