package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MailListComponentContract {
    interface View : ComponentBaseView {
        fun showRefreshProgress(show : Boolean)
        fun showMessages(messages : List<Message>)
    }

    interface Presenter : BasePresenter<MailListComponentContract.View> {
        fun setup(folder : Folder)
        fun setup(sender : Sender)
        fun refresh()
    }
}