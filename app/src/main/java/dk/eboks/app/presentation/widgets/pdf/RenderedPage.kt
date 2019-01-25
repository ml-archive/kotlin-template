package dk.eboks.app.presentation.widgets.pdf

import android.graphics.Bitmap

/**
 * Created by bison on 12-02-2018.
 */
class RenderedPage(
        var page: Bitmap? = null
) {
    val loaded: Boolean
        get() = page != null
}