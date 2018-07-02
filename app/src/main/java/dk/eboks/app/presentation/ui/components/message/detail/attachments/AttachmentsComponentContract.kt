package dk.eboks.app.presentation.ui.components.message.detail.attachments

import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface AttachmentsComponentContract {
    interface View : BaseView {
        fun updateView(message : Message)
        fun openExternalViewer(attachment: Content, filename: String, mimeType : String)
    }

    interface Presenter : BasePresenter<AttachmentsComponentContract.View> {
        fun openAttachment(content : Content)
        fun saveAttachment(content : Content)
    }
}