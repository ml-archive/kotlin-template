package dk.eboks.app.presentation.ui.mail.components.maillist

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
        fun showRefreshProgress(show: Boolean)
        fun showMessages(messages: List<Message>)
        fun appendMessages(messages: List<Message>)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(folder: Folder)
        fun setup(sender: Sender)
        fun refresh()
        fun loadNextPage()

//        fun updateMessage(message: Message)
        fun archiveMessages(selectedMessages: MutableList<Message>)
        fun markReadMessages(selectedMessages: MutableList<Message>, unread : Boolean)
        fun deleteMessages(selectedMessages: MutableList<Message>)
        fun moveMessages(folderId: Int, selectedMessages: MutableList<Message>)

        var isLoading : Boolean
    }
}