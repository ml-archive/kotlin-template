package dk.eboks.app.presentation.ui.debug.screens.user

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.keychain.interactors.user.CreateDebugUserInteractorImpl
import dk.eboks.app.keychain.interactors.user.CreateUserInteractor
import dk.eboks.app.profile.interactors.SaveUserInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DebugUserPresenter @Inject constructor(
    private val userSettingsManager: UserSettingsManager,
    private val createUserInteractor: CreateDebugUserInteractorImpl,
    private val saveUserInteractor: SaveUserInteractor,
    private val appConfig: AppConfig
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
        view {
            showLoginProviderSpinner(appConfig.loginProviders.values.toList())
            editUser?.let {
                showUser(it, userSettingsManager[it.id])
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
        val settings = userSettingsManager[user.id]
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
        view { close(true) }
    }

    override fun onCreateUserError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        view { close(false) }
    }

    override fun onSaveUserError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    companion object {
        var editUser: User? = null
    }
}