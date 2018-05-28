package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface LoginComponentContract {
    interface View : BaseView {
        fun setupView(loginProvider: LoginProvider?, user: User?, altLoginProviders: List<LoginProvider>)
        fun showActivationCodeDialog()
        fun showError(viewError: ViewError)
        fun addFingerPrintProvider()
        fun proceedToApp()
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun createUserAndLogin(email: String?, cpr: String?, verified: Boolean = false)
        fun updateLoginState(user: User, providerId: String, password: String, activationCode: String?)
        fun login()
        fun switchLoginProvider(provider: LoginProvider)
    }
}