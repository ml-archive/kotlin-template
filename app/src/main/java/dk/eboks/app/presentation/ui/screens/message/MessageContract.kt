package dk.eboks.app.presentation.ui.screens.message

import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MessageContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showTitle(message: Message)
    }

    interface Presenter : BasePresenter<MessageContract.View> {
    }
}