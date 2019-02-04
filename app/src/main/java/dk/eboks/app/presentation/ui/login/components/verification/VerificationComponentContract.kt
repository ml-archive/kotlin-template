package dk.eboks.app.presentation.ui.login.components.verification

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface VerificationComponentContract {
    interface View : BaseView

    interface Presenter : BasePresenter<View> {
        fun setupVerificationState(signupVerification: Boolean = false)
    }
}