package dk.eboks.app.presentation.ui.message.components.detail.document

import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface DocumentComponentContract {
    interface View : BaseView {
        fun updateView(message: Message)
        fun openExternalViewer(filename: String, mimeType: String)
    }

    interface Presenter : BasePresenter<DocumentComponentContract.View> {
        fun openExternalViewer(message: Message)
        fun saveAttachment(content: Content)
    }
}