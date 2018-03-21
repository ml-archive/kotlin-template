package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface LoginComponentContract {
    interface View : BaseView {
        fun setupView(loginProvider: LoginProvider?, user: User?, altLoginProviders: List<LoginProvider>)
        fun addFingerPrintProvider()
        fun proceedToApp()
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun createUserAndLogin(email: String?, cpr: String?, verified: Boolean = false)
        fun login(user : User, providerId : String)
        fun switchLoginProvider(provider: LoginProvider)
    }
}