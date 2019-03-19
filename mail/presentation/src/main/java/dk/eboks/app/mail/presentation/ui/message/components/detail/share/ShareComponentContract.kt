package dk.eboks.app.mail.presentation.ui.message.components.detail.share

import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ShareComponentContract {
    interface View : BaseView {
        fun updateView(message: Message)
        fun openExternalViewer(filename: String, mimeType: String)
    }

    interface Presenter : BasePresenter<View> {
        fun openExternalViewer(message: Message)
    }
}