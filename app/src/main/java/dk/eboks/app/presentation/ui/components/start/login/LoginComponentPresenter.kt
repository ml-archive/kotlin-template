package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.ContactPoint
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class LoginComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val loginInteractor: LoginInteractor
) :
        LoginComponentContract.Presenter,
        BasePresenterImpl<LoginComponentContract.View>(),
        LoginInteractor.Output {

    var altProviders: List<LoginProvider> = Config.getAlternativeLoginProviders()

    init {
        appState.state?.currentUser = null
        loginInteractor.output = this
    }

    override fun setup() {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let {
                setupLogin(it, it.lastLoginProviderId)
            }.guard {
                runAction { v ->
                    v.setupView(null, null, altProviders)
                }
            }
        }
    }

    private fun setupLogin(user: User?, provider: String?) {
        val lp = if (provider != null) Config.getLoginProvider(provider) else null
        runAction { v ->
            user?.let {
                // setup for existing user
                if (!it.verified) {   // user is not verified
                    v.setupView(loginProvider = lp, user = user, altLoginProviders = ArrayList())
                } else {
                    // user is verified
                    v.setupView(loginProvider = lp, user = user, altLoginProviders = altProviders)
                }
            }.guard {
                // setup for first time login
                v.setupView(loginProvider = lp, user = null, altLoginProviders = ArrayList())
            }
        }
    }

    override fun onLoginSuccess(response: AccessToken) {
        Timber.i("Login Success: $response")
        runAction { v ->
            v.proceedToApp()
        }
    }

    override fun onLoginDenied(error: ViewError) {
        Timber.w(" \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word!")
        runAction { v ->
            v.showProgress(false)
            v.showError(error)
        }
    }

    override fun onLoginError(error: ViewError) {
        Timber.e("Login Error!!")
        runAction { v ->
            v.showProgress(false)
            v.showError(error)
        }
    }

    override fun updateLoginState(userName: String, providerId: String, password: String, activationCode: String?) {
        appState.state?.loginState?.let { ls ->
            ls.userName = userName
            ls.userPassWord = password
            ls.userLoginProviderId = providerId
            ls.activationCode = activationCode

        }
    }

    override fun login() {
        appState.state?.loginState?.let {
            runAction { v->v.showProgress(true) }
            loginInteractor.input = LoginInteractor.Input(it)
            loginInteractor.run()
        }
    }

    override fun switchLoginProvider(provider: LoginProvider) {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let {
                setupLogin(it, provider.id)
            }.guard {
                setupLogin(
                        null,
                        provider.id
                )
            }
        }
    }

    override fun onLoginActivationCodeRequired() {
        runAction { v ->
            v.showActivationCodeDialog()
        }
    }

}