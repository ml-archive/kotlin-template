package dk.eboks.app.presentation.widgets

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import dk.eboks.app.util.dpToPx

/**
 * This will put a drawable below each row in the RecyclerView.
 *
 * Created by Christian on 5/8/2018.
 * @author   Christian
 * @since    5/8/2018.
 *
 * @param drawable The thing to draw below each view
 * @param indentationDp How many DP to indent from the LEFT edge
 * @param backgroundColor Sometimes you may need a background, otherwise transparent is used
 */
class DividerItemDecoration(private val drawable : Drawable = ColorDrawable(Color.GRAY),
                            private val indentationDp: Int = 0,
                            private val backgroundColor: Int = Color.TRANSPARENT)  : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom += drawable.intrinsicHeight
    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        for (i in 0 until parent.childCount - 1) { // not after the last
            val child = parent.getChildAt(i)

            val left = parent.paddingLeft + dpToPx(indentationDp)
            val top = child.bottom
            val right = parent.width - parent.paddingRight
            val bottom = child.bottom + drawable.intrinsicHeight

            val bg = ColorDrawable()
            bg.color = backgroundColor
            bg.setBounds(parent.paddingLeft, top, right, bottom) // same, but with no indentation
            bg.draw(c)

            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c)
        }
    }
}