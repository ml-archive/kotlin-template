package dk.eboks.app.presentation.ui.components.senders

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
interface SenderGroupsComponentContract {

    interface View : BaseView {
        fun showSenderGroups(senderGroups: List<SenderGroup>)
        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun getSenderGroups(sender: Sender)
    }
}