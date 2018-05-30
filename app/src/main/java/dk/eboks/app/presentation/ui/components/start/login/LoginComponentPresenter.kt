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
        val createUserInteractor: CreateUserInteractor,
        val loginInteractor: LoginInteractor
) :
        LoginComponentContract.Presenter,
        BasePresenterImpl<LoginComponentContract.View>(),
        CreateUserInteractor.Output,
        LoginInteractor.Output {

    var altProviders: List<LoginProvider> = Config.getAlternativeLoginProviders()

    init {
        appState.state?.currentUser = null
        createUserInteractor.output = this
        loginInteractor.output = this
    }

    override fun setup() {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let {
                setupLogin(it, it.lastLoginProvider)
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
        /*
        appState.state?.currentUser = user
        appState.save()
        */
        runAction { v -> v.proceedToApp() }
    }

    // all admire chnt's jurassic joke (its from '94 ffs :p)
    // Don't you worry - it's a UNIX system! It hasn't changed since the universe began anyway
    override fun onLoginDenied(error: ViewError) {
        Timber.w(" \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word!")
    }

    override fun onLoginError(error: ViewError) {
        Timber.e("Login Error!!")
    }

    override fun updateLoginState(user: User, providerId: String, password: String, activationCode: String?) {

        user.lastLoginProvider = providerId

        appState.state?.loginState?.let { ls ->
            ls.userName = user.cpr ?: user.getPrimaryEmail() ?: ""
            ls.userPassWord = password
            activationCode?.let {
                ls.activationCode = it
            }
        }
    }

    override fun login() {
        appState.state?.loginState?.let {
            loginInteractor.input = LoginInteractor.Input(it)
            loginInteractor.run()
        }
    }

    //    // TODO not much loggin going on
    override fun createUserAndLogin(email: String?, cpr: String?, verified: Boolean) {
        val provider = if (email != null) {
            Config.getLoginProvider("email")
        } else Config.getLoginProvider(
                "cpr"
        )

        val user = User(
                id = -1,
                name = "Enel Leranden",
                emails = arrayListOf(ContactPoint(email ?: "", true)),
                cpr = cpr,
                avatarUri = null,
                lastLoginProvider = provider?.id,
                verified = verified,
                hasFingerprint = false
        )

        createUserInteractor.input = CreateUserInteractor.Input(user, "tempPassword")
        createUserInteractor.run()
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

    override fun onCreateUser(user: User, numberOfUsers: Int) {
        Timber.i("User created $user")
    }

    override fun onCreateUserError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onLoginActivationCodeRequired() {
        runAction { it.showActivationCodeDialog() }
    }

    override fun setActivationCode(activationCode: String) {

    }
}