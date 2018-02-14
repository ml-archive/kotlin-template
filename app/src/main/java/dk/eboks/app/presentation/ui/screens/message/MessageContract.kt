package dk.eboks.app.presentation.ui.screens.message

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MessageContract {
    interface View : BaseView {
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<MessageContract.View> {
    }
}