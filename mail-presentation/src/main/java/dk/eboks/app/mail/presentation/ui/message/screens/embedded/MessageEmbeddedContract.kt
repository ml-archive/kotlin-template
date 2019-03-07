package dk.eboks.app.mail.presentation.ui.message.screens.embedded

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MessageEmbeddedContract {
    interface View : BaseView {
        fun addHeaderComponentFragment()
        fun addReplyButtonComponentFragment(message: Message)
        fun addSignButtonComponentFragment(message: Message)
        fun addNotesComponentFragment()
        fun addAttachmentsComponentFragment()
        fun addFolderInfoComponentFragment()
        fun addShareComponentFragment()
        fun addPaymentButton(payment: Payment)
        fun addPdfViewer()
        fun addImageViewer()
        fun addHtmlViewer()
        fun addTextViewer()
        fun showTitle(message: Message)
        fun setHighPeakHeight()
        fun setActionButton(message: Message)
        fun messageDeleted()
        fun updateFolderName(name: String)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun moveMessage(folder: Folder)
        fun deleteMessage()
        fun archiveMessage()
        fun markMessageRead()
        fun markMessageUnread()
    }
}