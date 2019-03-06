package dk.eboks.app.presentation.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import dk.eboks.app.R

/**
 * Created by bison on 23/11/15.
 */
class DottedViewPager : ViewPager {
    private lateinit var inactiveDot: Drawable
    private lateinit var activeDot: Drawable
    private var circleSize: Int = 0
    private var noCircles: Int = 0
    private var curCircle = 0
    private var margin: Int = 0
    private var marginBottom: Int = 0
    private var density: Float = 0.toFloat()
    private var screenWidth: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        density = resources.displayMetrics.density
        screenWidth = resources.displayMetrics.widthPixels
        activeDot = ContextCompat.getDrawable(context, R.drawable.circle_filled) ?: return
        inactiveDot = ContextCompat.getDrawable(context, R.drawable.circle_outline) ?: return
        circleSize = (density * 6.0f).toInt()
        margin = (density * 5.0f).toInt()
        marginBottom = (density * 7f).toInt()
    }

    fun setMarginBottom(dp: Int) {
        marginBottom = (density * dp.toFloat()).toInt()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (noCircles == 1) {
            return
        }

        val horiz_off = this.computeHorizontalScrollOffset()
        curCircle = currentItem

        // NLog.e(TAG, "dispatchDraw curCircle = " + curCircle + " noCircles = " + noCircles);
        val span_w = noCircles * (circleSize + margin)
        val startx = screenWidth / 2 - span_w / 2
        var offx = 0
        // int offy = getHeight() - marginBottom - circleSize;
        val offy = height + (density * 8f).toInt()

        for (i in 0 until noCircles) {
            offx = startx + i * (circleSize + margin)
            if (i != curCircle) {
                inactiveDot.setBounds(
                    horiz_off + offx,
                    offy,
                    horiz_off + offx + circleSize,
                    offy + circleSize
                )
                inactiveDot.draw(canvas)
            } else {
                activeDot.setBounds(
                    horiz_off + offx,
                    offy,
                    horiz_off + offx + circleSize,
                    offy + circleSize
                )
                activeDot.draw(canvas)
            }
        }
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        noCircles = adapter?.count ?: noCircles
    }

    companion object {
        private val TAG = DottedViewPager::class.java.simpleName
    }
}
