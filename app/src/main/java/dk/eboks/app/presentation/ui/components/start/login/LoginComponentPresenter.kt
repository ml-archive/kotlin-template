package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.authentication.PostAuthenticateUserInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
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
            }.guard { setupFirstLogin() }
        }
    }

    private fun setupFirstLogin() {
        val altproviders: MutableList<LoginProvider> = ArrayList()
        if (Config.isDK()) {
            Config.getLoginProvider("nemid")?.let { altproviders.add(it) }
        }
        if (Config.isNO()) {
            Config.getLoginProvider("idporten")?.let { altproviders.add(it) }
            Config.getLoginProvider("bankid_no")?.let { altproviders.add(it) }
        }
        if (Config.isSE()) {
            Config.getLoginProvider("bankid_se")?.let { altproviders.add(it) }
        }
        runAction { v -> v.setupView(null, null, altproviders) }
    }

    private fun setupLogin(user: User?, provider: String?) {
        val lp = if (provider != null) Config.getLoginProvider(provider) else null
        user?.let {
            if (!user.verified) {
                runAction { v ->
                    v.setupView(loginProvider = lp, user = user, altLoginProviders = ArrayList())
                    if (user.hasFingerprint) {
                        v.addFingerPrintProvider()
                    }
                }
            } else // user is verified
            {
                // add secure alternate login providers based on country
                val altproviders: MutableList<LoginProvider> = ArrayList()

                if (Config.isDK()) {
                    Config.getLoginProvider("nemid")?.let { altproviders.add(it) }
                }

                if (Config.isNO()) {
                    Config.getLoginProvider("idporten")?.let { altproviders.add(it) }
                    Config.getLoginProvider("bankid_no")?.let { altproviders.add(it) }
                }

                if (Config.isSE()) {
                    Config.getLoginProvider("bankid_se")?.let { altproviders.add(it) }
                }

                runAction { v ->
                    v.setupView(loginProvider = lp, user = user, altLoginProviders = altproviders)
                    if (user.hasFingerprint) {
                        v.addFingerPrintProvider()
                    }
                }
            }
        }.guard {
            runAction { v ->
                v.setupView(loginProvider = lp, user = null, altLoginProviders = ArrayList())
            }
        }
    }

    override fun onAuthenticationsSuccess(user: User, response: AccessToken) {
        Timber.i("Login Sucess: $response")
        appState.state?.currentUser = user
        appState.save()
        runAction { v -> v.proceedToApp() }
    }

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