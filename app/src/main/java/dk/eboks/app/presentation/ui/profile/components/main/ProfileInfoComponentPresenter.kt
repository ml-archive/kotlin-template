package dk.eboks.app.presentation.ui.profile.components.main

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ProfileInfoComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        private val saveUserInteractor: SaveUserInteractor,
        private val saveUserSettingsInteractor: SaveUserSettingsInteractor,
        val getUserProfileInteractor: GetUserProfileInteractor,
        val userSettingsManager: UserSettingsManager
) :
        ProfileInfoComponentContract.Presenter,
        BasePresenterImpl<ProfileInfoComponentContract.View>(),
        SaveUserInteractor.Output,
        GetUserProfileInteractor.Output {

    override var isUserVerified : Boolean = false

    override fun onViewCreated(view: ProfileInfoComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        saveUserInteractor.output = this
        getUserProfileInteractor.output = this
        isUserVerified = appState.state?.currentUser?.verified ?: false
        view.setName(appState.state?.currentUser?.name ?: "")
    }

    override fun loadUserData(showProgress : Boolean) {
        Timber.d("loadUserData")

        val currentUser = appState.state?.currentUser

        if (currentUser == null) {
            // TODO add some error handling
            Timber.e("Null Current User")
//            return
        }

        runAction { v->v.showProgress(showProgress) }

        getUserProfileInteractor.run()
    }

    override fun onGetUser(user: User) {
        runAction { v ->
            v.detachListeners()
            v.setName(user.name)
            v.setVerified(user.verified)
            v.showFingerprintOptionIfSupported()

            v.setProfileImage(user.avatarUri)

            appState.state?.currentSettings?.let {
                v.showFingerprintEnabled(it.hasFingerprint, it.lastLoginProviderId)
                v.showKeepMeSignedIn(it.stayLoggedIn)
            }
            v.showProgress(false)
            v.attachListeners()
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
        loadUserData(false)
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun doLogout() {
        appState.state?.currentSettings = null
        appState.state?.loginState?.userPassWord = ""
        appState.state?.loginState?.userName = ""
        appState.state?.loginState?.token = null
        appState.state?.openingState?.acceptPrivateTerms = false
        //activationCode still used ?
        appState.state?.loginState?.activationCode = null
        appState.save()
        view?.logout()
    }
}