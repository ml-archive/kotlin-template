package dk.eboks.app.presentation.ui.components.senders.register

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by Christian on 3/22/2018.
 * @author   Christian
 * @since    3/22/2018.
 */
interface RegistrationContract {

    interface View : BaseView {
        fun showSuccess()
        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun registerSender(sender: Sender) {}
        fun registerSenderGroup(senderId: Long, sendergroup: SenderGroup) {}
    }
}