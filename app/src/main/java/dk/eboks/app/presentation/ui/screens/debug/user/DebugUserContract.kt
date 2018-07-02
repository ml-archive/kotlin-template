package dk.eboks.app.presentation.ui.screens.debug.user

import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface DebugUserContract {
    interface View : BaseView {
        fun showLoginProviderSpinner(providers : List<LoginProvider>)
        fun close(gotoUsers : Boolean)
        fun showUser(user : User, userSettings: UserSettings)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun createUser(provider : LoginProvider, name : String, email : String?, cpr : String?, verified : Boolean, fingerprint : Boolean)
        fun saveUser(user: User, loginProviderId: String, hasFingerprint: Boolean)
    }
}