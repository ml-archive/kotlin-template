package dk.eboks.app.presentation.ui.profile.components.drawer

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.authentication.TestLoginInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class FingerPrintComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val userSettingsManager: UserSettingsManager,
        private val encryptUserLoginInfoInteractor: EncryptUserLoginInfoInteractor,
        private val saveUserInteractor: SaveUserInteractor,
        private val testLoginInteractor: TestLoginInteractor
) :
    FingerPrintComponentContract.Presenter,
    BasePresenterImpl<FingerPrintComponentContract.View>(),
    EncryptUserLoginInfoInteractor.Output,
    SaveUserInteractor.Output,
    TestLoginInteractor.Output {

    init {
        encryptUserLoginInfoInteractor.output = this
        saveUserInteractor.output = this
        testLoginInteractor.output = this
    }

    override fun onViewCreated(view: FingerPrintComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        loadUserState()
    }

    private fun loadUserState() {
        val lastLoginProvider: String? = appState.state?.currentSettings?.lastLoginProviderId

        if (lastLoginProvider == null) {
            showErrorMessage()
            return
        }

        if (lastLoginProvider.equals("email", false)) {
            view?.setProviderMode(LoginInfoType.EMAIL)
        } else {
            view?.setProviderMode(LoginInfoType.SOCIAL_SECURITY)
        }
    }

    private fun showErrorMessage() {
        val errorMessage =
            ViewError(Translation.error.genericTitle, Translation.error.genericMessage, true, true)
        view?.showErrorDialog(errorMessage)
    }

    override fun verifyLoginCredentials() {
        val loginInfo = view?.getUserLoginInfo()
        Timber.d("verifyLoginCredentials: $loginInfo")
        runAction { v -> v.showProgress(true) }
        loginInfo?.let {
            it.actvationCode =
                userSettingsManager.get(appState.state?.currentUser?.id ?: 0).activationCode ?: ""
            testLoginInteractor.input = TestLoginInteractor.Input(
                loginInfo.socialSecurity,
                loginInfo.password,
                it.actvationCode
            )
            testLoginInteractor.run()
        }
    }

    override fun encryptUserLoginInfo() {
        val loginInfo = view?.getUserLoginInfo()

        Timber.d("encryptUserLoginInfo: $loginInfo")

        loginInfo?.let {
            it.actvationCode =
                userSettingsManager.get(appState.state?.currentUser?.id ?: 0).activationCode ?: ""
            encryptUserLoginInfoInteractor.output = this
            encryptUserLoginInfoInteractor.input = EncryptUserLoginInfoInteractor.Input(loginInfo)
            encryptUserLoginInfoInteractor.run()
        }
    }

    private fun saveFingerprintEnrollmentState() {
        Timber.d("saveFingerprintEnrollmentState")

        val currentUser = appState.state?.currentUser

        if (currentUser == null) {
            val viewError = ViewError(
                Translation.error.genericTitle,
                Translation.error.genericMessage,
                true,
                true
            )
            runAction { it.showErrorDialog(viewError) }
            return
        }

        userSettingsManager.removeFingerprintFromYall()
        userSettingsManager[currentUser.id].hasFingerprint = true
        userSettingsManager.save()

        saveUserInteractor.output = this
        saveUserInteractor.input = SaveUserInteractor.Input(currentUser)
        saveUserInteractor.run()
    }

    /**
     * EncryptUserLoginInteractor callbacks
     */
    override fun onSuccess() {
        saveFingerprintEnrollmentState()
    }

    override fun onError(e: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(e)
        }
    }

    /**
     * SaveUserInteractor callbacks
     */
    override fun onSaveUser(user: User, numberOfUsers: Int) {
        runAction { v ->
            v.finishView()
        }
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    /**
     * TestLoginInteractor callbacks
     */
    override fun onTestLoginSuccess() {
        runAction { v ->
            v.proceedAfterLoginVerification()
        }
    }

    override fun onTestLoginDenied(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onTestLoginError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}