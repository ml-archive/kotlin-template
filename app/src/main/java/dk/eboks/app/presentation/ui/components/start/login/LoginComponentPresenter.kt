package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.*
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class LoginComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val userSettingsManager: UserSettingsManager,
        val decryptUserLoginInfoInteractor: DecryptUserLoginInfoInteractor,
        val loginInteractor: LoginInteractor
) :
        LoginComponentContract.Presenter,
        BasePresenterImpl<LoginComponentContract.View>(),
        DecryptUserLoginInfoInteractor.Output,
        LoginInteractor.Output {

    var altProviders: List<LoginProvider> = Config.getAlternativeLoginProviders()

    init {
        appState.state?.currentUser = null
        appState.state?.currentSettings = null
        loginInteractor.output = this
        decryptUserLoginInfoInteractor.output = this
    }

    override fun setup() {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let {
                val settings = userSettingsManager.get(it.id)
                Timber.d("Loaded $settings")
                var provider = settings.lastLoginProviderId
                // Test-uses has "test" prefix, as in 'DebugUsersComponentPresenter'
                if (BuildConfig.DEBUG && true == provider?.contains("test")) {
                    provider = provider.removePrefix("test")// remove the prefix
//                    setupLogin(it, provider)
                    appState.state?.loginState?.userLoginProviderId = provider
                    appState.save()
                    login()
                } else {
                    setupLogin(it, provider)
                }
            }.guard {
                runAction { v ->
                    v.setupView(null, null, UserSettings(0), altProviders)
                }
            }
        }
    }

    private fun setupLogin(user: User?, provider: String?) {
        val lp = if (provider != null) {
            Config.getLoginProvider(provider)
        } else {
            null
        }
        runAction { v ->
            user?.let {
                // setup for existing user
                val settings = userSettingsManager.get(it.id)
                if (!it.verified) {   // user is not verified
                    v.setupView(loginProvider = lp, user = user, settings = settings, altLoginProviders = ArrayList())
                } else {
                    // user is verified
                    v.setupView(loginProvider = lp, user = user, settings = settings, altLoginProviders = altProviders)
                }
            }.guard {
                // setup for first time login
                v.setupView(loginProvider = lp, user = null, settings = UserSettings(0), altLoginProviders = ArrayList())
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

    override fun fingerPrintConfirmed(user: User) {
        runAction { v -> v.showProgress(true) }

        decryptUserLoginInfoInteractor.run()
    }

    override fun onDecryptSuccess(loginInfo: LoginInfo) {
        val providerId = if(loginInfo.type==LoginInfoType.EMAIL) {
            "email"
        } else {
            "cpr"
        }
        updateLoginState(loginInfo.socialSecurity, providerId, loginInfo.password, loginInfo.actvationCode)
        login()
    }

    override fun onDecryptError(error: ViewError) {
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
            runAction { v -> v.showProgress(true) }
            loginInteractor.input = LoginInteractor.Input(it)
            loginInteractor.run()
        }
    }

    override fun switchLoginProvider(provider: LoginProvider) {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let {
                setupLogin(it, provider.id)
            }.guard {
                setupLogin(null, provider.id)
            }
        }
    }

    override fun onLoginActivationCodeRequired() {
        runAction { v ->
            v.showActivationCodeDialog()
        }
    }

}