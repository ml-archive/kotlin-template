package dk.eboks.app.presentation.ui.components.message.detail.share

import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ShareComponentContract {
    interface View : BaseView {
        fun updateView(message : Message)
        fun openExternalViewer(filename: String, mimeType : String)
    }

    interface Presenter : BasePresenter<ShareComponentContract.View> {
        fun openExternalViewer(message : Message)
    }
}