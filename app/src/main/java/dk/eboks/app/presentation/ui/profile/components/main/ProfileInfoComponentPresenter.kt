package dk.eboks.app.presentation.ui.profile.components.main

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ProfileInfoComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        private val saveUserInteractor: SaveUserInteractor,
        private val saveUserSettingsInteractor: SaveUserSettingsInteractor,
        val getUserProfileInteractor: GetUserProfileInteractor
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

        runAction { v->v.showProgress(true) }

        getUserProfileInteractor.run()
    }

    override fun onGetUser(user: User) {
        runAction { v ->
            v.setName(user.name)
            v.setVerified(user.verified)
            v.showFingerprintOptionIfSupported()

            v.setProfileImage(user.avatarUri)

            appState.state?.currentSettings?.let {
                v.showFingerprintEnabled(it.hasFingerprint, it.lastLoginProviderId)
                v.showKeepMeSignedIn(it.stayLoggedIn)
            }
            v.setupListeners(user.verified)
            v.showProgress(false)
        }
    }

    override fun onGetUserError(error: ViewError) {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun saveUserImg(uri: String) {
        appState.state?.currentUser?.avatarUri = uri
        appState.save()

        appState.state?.currentSettings?.let {
            saveUserSettingsInteractor.input = SaveUserSettingsInteractor.Input(it)
            saveUserSettingsInteractor.run()
        }
    }

    override fun enableUserFingerprint(enable: Boolean) {
        appState.state?.currentSettings?.hasFingerprint = enable
        appState.save()

        appState.state?.currentSettings?.let {
            saveUserSettingsInteractor.input = SaveUserSettingsInteractor.Input(it)
            saveUserSettingsInteractor.run()
        }
    }

    override fun enableKeepMeSignedIn(enable: Boolean) {
        appState.state?.currentSettings?.stayLoggedIn = enable
        appState.save()

        appState.state?.currentSettings?.let {
            saveUserSettingsInteractor.input = SaveUserSettingsInteractor.Input(it)
            saveUserSettingsInteractor.run()
        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        loadUserData()
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun doLogout() {
        appState.state?.currentSettings = null
        appState.state?.loginState?.token = null
        appState.save()
        view?.logout()
    }
}