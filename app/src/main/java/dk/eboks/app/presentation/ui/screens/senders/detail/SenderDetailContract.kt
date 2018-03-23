package dk.eboks.app.presentation.ui.screens.senders.detail

import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
* Created by bison on 07-11-2017.
* @author   bison
* @since    07-11-2017.
*/
interface SenderDetailContract {
    interface View : BaseView {
        fun showSuccess()
        fun showSender(sender: Sender)
        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadSender(id : Long)
        fun registerSender(id: Long)
        fun unregisterSender(id: Long)
    }
}