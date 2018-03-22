package dk.eboks.app.presentation.ui.screens.debug.user

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class DebugUserPresenter(val appStateManager: AppStateManager, val createUserInteractor: CreateUserInteractor, val saveUserInteractor: SaveUserInteractor) :
        DebugUserContract.Presenter,
        BasePresenterImpl<DebugUserContract.View>(),
        CreateUserInteractor.Output,
        SaveUserInteractor.Output
{
    init {
        createUserInteractor.output = this
        saveUserInteractor.output = this
    }

    override fun setup()
    {
        runAction { v->
            v.showLoginProviderSpinner(Config.loginProviders.values.toList())
            editUser?.let {
                v.showUser(it)
                editUser = null
            }
        }
    }

    override fun createUser(provider: LoginProvider, name: String, email: String?, cpr: String?, verified: Boolean, fingerprint: Boolean) {
        val user : User = User(
                id = -1,
                name = name,
                email = email,
                cpr = cpr,
                avatarUri = null,
                lastLoginProvider = provider.id,
                verified = verified,
                hasFingerprint = fingerprint
        )
        createUserInteractor.input = CreateUserInteractor.Input(user)
        createUserInteractor.run()
    }

    override fun saveUser(user: User) {
        saveUserInteractor.input = SaveUserInteractor.Input(user)
        saveUserInteractor.run()
    }

    override fun onCreateUser(user: User, numberOfUsers : Int) {
        runAction { v->v.close(numberOfUsers == 1) }
    }

    override fun onCreateUserError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        runAction { v->v.close(false) }
    }

    override fun onSaveUserError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    companion object {
        var editUser : User? = null
    }
}