package dk.eboks.app.presentation.ui.login.components

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ActivationCodeComponentContract {
    interface View : BaseView {
        fun proceedToApp()
        fun showError(error: String?)
        fun setDebugUp(activationCode: String?)
        fun showProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun updateLoginState(activationCode: String?)
        fun login()
    }
}