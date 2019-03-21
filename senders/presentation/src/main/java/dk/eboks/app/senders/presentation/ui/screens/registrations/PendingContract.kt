package dk.eboks.app.senders.presentation.ui.screens.registrations

import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by Christian on 3/28/2018.
 * @author Christian
 * @since 3/28/2018.
 */
interface PendingContract {
    interface View : BaseView {
        fun showPendingRegistrations(registrations: List<CollectionContainer>)
        fun showRegistrationSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun loadPending()

        fun registerSender(id: Long)
        fun unregisterSender(id: Long)

        fun registerSegment(id: Long)
        fun unregisterSegment(id: Long)
    }
}