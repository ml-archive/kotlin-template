package dk.eboks.app.presentation.ui.components.start.signup

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SignupComponentContract {
    interface SignupView : BaseView {
        fun showProgress(show: Boolean)
    }

    interface NameMailView : SignupView {
        fun showSignupMail(exists: Boolean)
        fun showSignupMailError(error: ViewError)
    }

    interface PasswordView : SignupView {

    }

    interface MMView : SignupView {
        fun ssnExists(ssnExisits: Boolean)
    }

    interface VerificationView : SignupView {

    }

    interface TermsView : SignupView {
        fun showSignupCompleted()
    }

    interface CompletedView : SignupView {
    }

    interface Presenter : BasePresenter<SignupView> {
        fun setPassword(password: String)
        fun loginUser()
        fun confirmMail(email: String, name: String)
        fun createUser()
        fun verifySSN(ssn: String)
    }
}