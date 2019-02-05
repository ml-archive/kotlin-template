package dk.eboks.app.presentation.ui.debug.screens.user

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.user.CreateDebugUserInteractorImpl
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class DebugUserPresenter(
    val appStateManager: AppStateManager,
    val userSettingsManager: UserSettingsManager,
    val createUserInteractor: CreateDebugUserInteractorImpl,
    val saveUserInteractor: SaveUserInteractor
) :
    DebugUserContract.Presenter,
    BasePresenterImpl<DebugUserContract.View>(),
    CreateUserInteractor.Output,
    SaveUserInteractor.Output {
    init {
        createUserInteractor.output = this
        saveUserInteractor.output = this
    }

    override fun setup() {
        runAction { v ->
            v.showLoginProviderSpinner(Config.loginProviders.values.toList())
            editUser?.let {
                v.showUser(it, userSettingsManager.get(it.id))
                editUser = null
            }
        }
    }

    override fun createUser(
        provider: LoginProvider,
        name: String,
        email: String?,
        cpr: String?,
        verified: Boolean,
        fingerprint: Boolean
    ) {
        val user = User(
            id = -1,
            name = name,
            identity = cpr,
            avatarUri = null,
            verified = verified
        )

        user.setPrimaryEmail(email)
        val settings = userSettingsManager.get(user.id)
        settings.lastLoginProviderId = provider.id
        settings.hasFingerprint = fingerprint
        userSettingsManager.put(settings)

        createUserInteractor.input = CreateUserInteractor.Input(user, "temp")
        createUserInteractor.run()
    }

    override fun saveUser(user: User, loginProviderId: String, hasFingerprint: Boolean) {
        val settings = userSettingsManager[user.id]
        settings.lastLoginProviderId = loginProviderId
        settings.hasFingerprint = hasFingerprint
        userSettingsManager.put(settings)

        saveUserInteractor.input = SaveUserInteractor.Input(user)
        saveUserInteractor.run()
    }

    override fun onCreateUser(user: User) {
        runAction { v -> v.close(true) }
    }

    override fun onCreateUserError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        runAction { v -> v.close(false) }
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    companion object {
        var editUser: User? = null
    }
}