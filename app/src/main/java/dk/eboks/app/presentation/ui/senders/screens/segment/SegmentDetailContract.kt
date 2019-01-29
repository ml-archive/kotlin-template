package dk.eboks.app.presentation.ui.senders.screens.segment

import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
* Created by chnt on 03-27-2018
*/
interface SegmentDetailContract {
    interface View : BaseView {
        fun showSuccess()
        fun toggleLoading(enabled: Boolean)
        fun showSegment(segment: Segment)
        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadSegment(id : Long)
        fun registerSegment(id: Long)
        fun unregisterSegment(id: Long)
    }
}