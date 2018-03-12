package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.internal.LoginState
import dk.eboks.app.domain.models.internal.User
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class LoginComponentPresenter @Inject constructor(val appState: AppStateManager, val createUserInteractor: CreateUserInteractor) :
        LoginComponentContract.Presenter,
        BasePresenterImpl<LoginComponentContract.View>(),
        CreateUserInteractor.Output
{

    init {
        createUserInteractor.output = this
        appState.state?.loginState?.let { state->
            state.selectedUser?.let { setupRelogin(it) }.guard { setupFirstLogin() }
        }
    }

    private fun setupFirstLogin()
    {
        val altproviders : MutableList<LoginProvider> = ArrayList()
        if(Config.isDK()) {
            Config.getLoginProvider("nemid")?.let { altproviders.add(it) }
        }
        if(Config.isNO()) {
            Config.getLoginProvider("idporten")?.let { altproviders.add(it) }
            Config.getLoginProvider("bankid_no")?.let { altproviders.add(it) }
        }
        if(Config.isSE()) {
            Config.getLoginProvider("bankid_se")?.let { altproviders.add(it) }
        }
        runAction { v-> v.setupView(null, null, altproviders) }
    }

    private fun setupRelogin(user: User)
    {
        if(!user.verified)
        {
            runAction { v->
                v.setupView(loginProvider = user.lastLoginProvider, user = user, altLoginProviders = ArrayList())
                if(user.hasFingerprint)
                    v.addFingerPrintProvider()
            }
        }
        else // user is verified
        {
            // add secure alternate login providers based on country
            val altproviders : MutableList<LoginProvider> = ArrayList()
            if(Config.isDK()) {
                Config.getLoginProvider("nemid")?.let { altproviders.add(it) }
            }
            if(Config.isNO()) {
                Config.getLoginProvider("idporten")?.let { altproviders.add(it) }
                Config.getLoginProvider("bankid_no")?.let { altproviders.add(it) }
            }
            if(Config.isSE()) {
                Config.getLoginProvider("bankid_se")?.let { altproviders.add(it) }
            }
            runAction {
                v-> v.setupView(loginProvider = user.lastLoginProvider, user = user, altLoginProviders = altproviders)
                if(user.hasFingerprint)
                    v.addFingerPrintProvider()
            }
        }
    }

    // TODO not much loggin going on
    override fun createUserAndLogin(email: String?, cpr: String?) {
        val provider = if(email != null) Config.getLoginProvider("email") else Config.getLoginProvider("cpr")
        val user : User = User(id = -1, name = "Name McLastName", email = email, cpr = cpr, avatarUri = null, lastLoginProvider = provider, verified = false, hasFingerprint = false)

        createUserInteractor.input = CreateUserInteractor.Input(user)
        createUserInteractor.run()
    }

    override fun onCreateUser(user: User) {
        Timber.e("User created $user")
    }

    override fun onCreateUserError(msg: String) {
        Timber.e(msg)
    }
}