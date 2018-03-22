package dk.eboks.app.presentation.ui.screens.message

import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MessageContract {
    interface View : BaseView {
        fun showTitle(message: Message)
    }

    interface Presenter : BasePresenter<MessageContract.View> {
    }
}