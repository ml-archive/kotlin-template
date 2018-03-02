package dk.eboks.app.presentation.ui.components.signup

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SignupComponentContract {
    interface SignupView : BaseView {
        fun showError()
    }

    interface NameMailView : SignupView {

    }

    interface PasswordView : SignupView {

    }

    interface VerifyView : SignupView {

    }

    interface TermsView : SignupView {

    }

    interface CompletedView : SignupView {

    }

    interface Presenter : BasePresenter<SignupView> {
    }
}