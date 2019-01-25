package dk.eboks.app.presentation.ui.message.screens

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MessageContract {
    interface View : BaseView {
        fun showTitle(message: Message)
        fun addHeaderComponentFragment()
        fun addDocumentComponentFragment()
        fun addReplyButtonComponentFragment(message: Message)
        fun addNotesComponentFragment()
        fun addAttachmentsComponentFragment()
        fun addFolderInfoComponentFragment()
        fun addShareComponentFragment()
        fun messageDeleted()
        fun setActionButtons(message: Message)
        fun updateFolderName(name: String)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun moveMessage(folder : Folder)
        fun deleteMessage()
        fun archiveMessage()
        fun markAsUnread(unread: Boolean)
    }
}