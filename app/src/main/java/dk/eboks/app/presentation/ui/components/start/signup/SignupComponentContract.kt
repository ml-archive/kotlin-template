package dk.eboks.app.presentation.ui.components.start.signup

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SignupComponentContract {
    interface SignupView : BaseView {
        fun showError()
        fun showProgress(show : Boolean)
    }

    interface NameMailView : SignupView {

    }

    interface PasswordView : SignupView {

    }

    interface MMView : SignupView {

    }

    interface VerificationView : SignupView {

    }

    interface TermsView : SignupView {

    }

    interface CompletedView : SignupView {

    }

    interface Presenter : BasePresenter<SignupView> {
    }
}