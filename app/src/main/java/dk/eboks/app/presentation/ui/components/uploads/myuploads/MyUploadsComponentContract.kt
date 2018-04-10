package dk.eboks.app.presentation.ui.components.uploads.myuploads

import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView
import dk.eboks.app.presentation.base.ComponentBaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MyUploadsComponentContract {
    interface View : ComponentBaseView {
        fun showRefreshProgress(show: Boolean)
        fun showMessages(messages: List<Message>)
    }

    interface Presenter : BasePresenter<MyUploadsComponentContract.View> {
        fun openMessage(message: Message)
        fun refresh()
    }
}