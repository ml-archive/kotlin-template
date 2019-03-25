package dk.eboks.app.senders.presentation.ui.screens.detail

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 * @author bison
 * @since 07-11-2017.
 */
interface SenderDetailContract {
    interface View : BaseView {
        fun showSuccess()
        fun showSender(sender: Sender)
        fun showError(message: String)
        fun toggleLoading(enabled: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun loadSender(id: Long)
        fun registerSender(id: Long)
        fun unregisterSender(id: Long)
        fun registerViaLink(id: Long)
    }
}