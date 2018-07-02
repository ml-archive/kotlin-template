package dk.eboks.app.presentation.ui.screens.message.embedded

import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MessageEmbeddedContract {
    interface View : BaseView {
        fun addHeaderComponentFragment()
        fun addReplyButtonComponentFragment(message: Message)
        fun addNotesComponentFragment()
        fun addAttachmentsComponentFragment()
        fun addFolderInfoComponentFragment()
        fun addShareComponentFragment()
        fun addPdfViewer()
        fun addImageViewer()
        fun addHtmlViewer()
        fun addTextViewer()
        fun showTitle(message : Message)
        fun setHighPeakHeight()
    }

    interface Presenter : BasePresenter<MessageEmbeddedContract.View> {
        fun setup()
    }
}