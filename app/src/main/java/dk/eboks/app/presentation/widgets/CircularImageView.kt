package dk.eboks.app.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by bison on 30/01/18.
 */
class CircularImageView : ImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attributes: AttributeSet?) : super(context, attributes)
    constructor(context: Context?, attributes: AttributeSet?, defStyle: Int) : super(
        context,
        attributes,
        defStyle
    )
}