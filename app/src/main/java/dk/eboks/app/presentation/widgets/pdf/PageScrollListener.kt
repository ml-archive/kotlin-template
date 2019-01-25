package dk.eboks.app.presentation.widgets.pdf

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class PageScrollListener(val listener: OnPageScrollChangeListener, val snapHelper: SnapHelper) : RecyclerView.OnScrollListener() {

    private var currentPosition = RecyclerView.NO_POSITION
        set(value) {
            field = value
            listener.onPageChanged(value)
        }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
               val snapPosition = snapHelper.getSnapPosition(recyclerView)
                val pageChanged = snapPosition != currentPosition
                if (pageChanged) {
                    currentPosition = snapPosition
                }
        }
    }

    interface OnPageScrollChangeListener {
        fun onPageChanged(newPageNumber: Int)
    }

    private fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)

    }


}

