package dk.eboks.app.presentation.ui.components.profile.main

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ProfileInfoComponentPresenter @Inject constructor(val appState: AppStateManager) :
        ProfileInfoComponentContract.Presenter,
        BasePresenterImpl<ProfileInfoComponentContract.View>() {


    override fun loadUserData() {
        Timber.d("loadUserData")

        val currentUser = appState.state?.currentUser

        if (currentUser == null) {
            // TODO add some error handling
            Timber.e("Null Current User")
            return
        }

        runAction { v->
            v.setName(currentUser.name)
            v.setProfileImage(currentUser.avatarUri)
            v.setFingerprintEnabled(currentUser.hasFingerprint, currentUser.lastLoginProvider)
            v.setVerified(currentUser.verified)
            v.setKeepMeSignedIn(false)
        }
    }

    override fun doLogout() {

    }
}