package dk.eboks.app.presentation.ui.components.profile.drawer

import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class FingerHintComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val encryptUserLoginInfoInteractor: EncryptUserLoginInfoInteractor,
        private val saveUserInteractor: SaveUserInteractor
) : FingerHintComponentContract.Presenter,
    BasePresenterImpl<FingerHintComponentContract.View>(),
    EncryptUserLoginInfoInteractor.Output,
    SaveUserInteractor.Output {

    override fun encryptUserLoginInfo() {
        val loginInfo = view?.getUserLoginInfo()

        Timber.d("encryptUserLoginInfo: %s", loginInfo)

        loginInfo?.let {
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
            view?.showErrorDialog(viewError)
            return
        }

        currentUser.hasFingerprint = true

        saveUserInteractor.output = this
        saveUserInteractor.input = SaveUserInteractor.Input(currentUser)
        saveUserInteractor.run()
    }

    override fun onSuccess() {
        Timber.d("onSuccess")
        saveFingerprintEnrollmentState()
    }

    override fun onError(e: ViewError) {
        Timber.e("onError")
        view?.showErrorDialog(e)
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        Timber.d("onSaveUser")
        view?.finishView()
    }

    override fun onSaveUserError(error: ViewError) {
        Timber.d("onSaveUserError")
        view?.showErrorDialog(error)
    }
}