package dk.eboks.app.presentation.ui.components.start.signup

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SignupComponentContract {
    interface SignupView : BaseView {
        fun showError()
        fun showProgress(show: Boolean)
    }

    interface NameMailView : SignupView {
        fun showSignupMail(exists: Boolean)
        fun showSignupMailError(error: ViewError)
    }

    interface PasswordView : SignupView {

    }

    interface MMView : SignupView {

    }

    interface VerificationView : SignupView {

    }

    interface TermsView : SignupView {
        fun showUserCreated()
        fun showUserCreatedError()
    }

    interface CompletedView : SignupView {
        fun doLogin()
    }

    interface Presenter : BasePresenter<SignupView> {
        fun setPassword(password: String)
        fun createUserAndLogin()
        fun confirmMail(email: String, name: String)
        fun createUser()
    }
}