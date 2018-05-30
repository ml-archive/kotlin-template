package dk.eboks.app.presentation.ui.components.profile.main

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ProfileInfoComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        private val saveUserInteractor: SaveUserInteractor,
        val getUserProfileInteractor : GetUserProfileInteractor
) :
        ProfileInfoComponentContract.Presenter,
        BasePresenterImpl<ProfileInfoComponentContract.View>(),
        SaveUserInteractor.Output,
        GetUserProfileInteractor.Output {
    override fun onViewCreated(view: ProfileInfoComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        saveUserInteractor.output = this
        getUserProfileInteractor.output = this
    }

    override fun loadUserData() {
        Timber.d("loadUserData")

        val currentUser = appState.state?.currentUser

        if (currentUser == null) {
            // TODO add some error handling
            Timber.e("Null Current User")
//            return
        }

     getUserProfileInteractor.run()
    }

    override fun onGetUser(currentUser: User) {

        runAction { v ->
//            v.setName(currentUser.name)
            v.setName(currentUser.id.toString() + "")
            v.setProfileImage(currentUser.avatarUri)
            v.setFingerprintEnabled(currentUser.hasFingerprint, currentUser.lastLoginProvider)
            v.setVerified(currentUser.verified)
            v.setKeepMeSignedIn(false)
        }
    }

    override fun onGetUserError(error: ViewError) {
        Timber.e("Null Current User")
    }

    override fun saveUserImg(uri: String) {
        appState.state?.currentUser?.avatarUri = uri
    }

    override fun enableUserFingerprint(isEnabled: Boolean) {
        appState.state?.currentUser?.hasFingerprint = isEnabled
        appState.save()

        appState.state?.currentUser?.let {
            saveUserInteractor.input = SaveUserInteractor.Input(it)
            saveUserInteractor.run()
        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        loadUserData()
    }

    override fun onSaveUserError(error: ViewError) {
        view?.showErrorDialog(error)
    }

    override fun doLogout() {

    }
}