package dk.eboks.app.presentation.ui.screens.message.opening

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MessageOpeningContract {
    interface View : BaseView {
        fun setPrivateSenderWarningFragment()
    }

    interface Presenter : BasePresenter<View> {
        fun signalMessageOpenDone()
    }
}