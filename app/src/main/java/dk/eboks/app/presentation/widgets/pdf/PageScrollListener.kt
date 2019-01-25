package dk.eboks.app.presentation.widgets.pdf

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import timber.log.Timber

class PageScrollListener(val listener: OnPageScrollChangeListener) : RecyclerView.OnScrollListener() {

    private var currentPosition = RecyclerView.NO_POSITION
        set(value) {
            field = value
            if (value != RecyclerView.NO_POSITION)
                listener.onPageChanged(value)
        }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visiblePosition = recyclerView.linearLayoutManager?.findFirstVisibleItemPosition() ?: currentPosition
        if (visiblePosition != currentPosition) {
            currentPosition = visiblePosition
        }
    }

    interface OnPageScrollChangeListener {
        fun onPageChanged(newPageNumber: Int)
    }


    private val RecyclerView.linearLayoutManager : LinearLayoutManager?
        get() = layoutManager as? LinearLayoutManager?

}

