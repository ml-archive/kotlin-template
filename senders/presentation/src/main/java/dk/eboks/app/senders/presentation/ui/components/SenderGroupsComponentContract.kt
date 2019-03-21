package dk.eboks.app.senders.presentation.ui.components

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by Christian on 3/15/2018.
 * @author Christian
 * @since 3/15/2018.
 */
interface SenderGroupsComponentContract {

    interface View : BaseView {
        fun showSenderGroups(sender: Sender)
        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun getSenderGroups(sender: Sender)
    }
}