package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.authentication.PostAuthenticateUserInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginResponse
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
        val postAuthenticateUserInteractor: PostAuthenticateUserInteractor
) :
        LoginComponentContract.Presenter,
        BasePresenterImpl<LoginComponentContract.View>(),
        CreateUserInteractor.Output,
        PostAuthenticateUserInteractor.Output {

    var altProviders : List<LoginProvider> = Config.getAlternativeLoginProviders()

    init {
        appState.state?.currentUser = null
        createUserInteractor.output = this
        postAuthenticateUserInteractor.output = this
    }

    override fun setup() {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let {
                setupLogin(
                        it,
                        it.lastLoginProvider
                )
            }.guard {
                runAction { v -> v.setupView(null, null, altProviders) }
            }
        }
    }

    private fun setupLogin(user: User?, provider: String?) {
        val lp = if (provider != null) Config.getLoginProvider(provider) else null
        runAction { v->
            user?.let { // setup for existing user
                if (!user.verified) {   // user is not verified
                    v.setupView(loginProvider = lp, user = user, altLoginProviders = ArrayList())
                } else {
                    // user is verified
                    v.setupView(loginProvider = lp, user = user, altLoginProviders = altProviders)
                }
                if (user.hasFingerprint) {
                    v.addFingerPrintProvider()
                }
            }.guard {   // setup for first time login
                v.setupView(loginProvider = lp, user = null, altLoginProviders = ArrayList())
            }
        }
    }

    override fun onAuthenticationsSuccess(user: User, response: LoginResponse) {
        Timber.i("Login Sucess: $response")
        appState.state?.currentUser = user
        appState.save()
        runAction { v -> v.proceedToApp() }
    }

    // all admire chnt's jurassic joke (its from '94 ffs :p)
    override fun onAuthenticationsDenied(error: ViewError) {
        Timber.w(" \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word! \nUh uh uhhh - you didn't say the magic word!")
    }

    override fun onAuthenticationsError(error: ViewError) {
        Timber.e("Login Error!!")
    }

    override fun login(user: User, providerId: String, password: String, activationCode: String?) {
        user.lastLoginProvider = providerId
        postAuthenticateUserInteractor.input = PostAuthenticateUserInteractor.Input(user, password, activationCode)
        postAuthenticateUserInteractor.run()

    }

    // TODO not much loggin going on
    override fun createUserAndLogin(email: String?, cpr: String?, verified: Boolean) {
        val provider = if (email != null) Config.getLoginProvider("email") else Config.getLoginProvider(
                "cpr"
        )
        val user: User = User(
                id = -1,
                name = "Name McLastName",
                email = email,
                cpr = cpr,
                avatarUri = null,
                lastLoginProvider = provider?.id,
                verified = verified,
                hasFingerprint = false
        )

        createUserInteractor.input = CreateUserInteractor.Input(user)
        createUserInteractor.run()
    }

    override fun switchLoginProvider(provider: LoginProvider) {
        appState.state?.loginState?.let { state ->
            state.selectedUser?.let { setupLogin(it, provider.id) }.guard {
                setupLogin(
                        null,
                        provider.id
                )
            }
        }
    }

    override fun onCreateUser(user: User, numberOfUsers: Int) {
        Timber.e("User created $user")
    }

    override fun onCreateUserError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}