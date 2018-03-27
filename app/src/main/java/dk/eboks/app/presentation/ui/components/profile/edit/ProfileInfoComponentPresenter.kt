package dk.eboks.app.presentation.ui.components.profile.edit

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ProfileInfoComponentPresenter @Inject constructor(val appState: AppStateManager) :
        ProfileInfoComponentContract.Presenter,
        BasePresenterImpl<ProfileInfoComponentContract.View>() {

    override fun onViewCreated(view: ProfileInfoComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        loadUserData()
    }

    override fun loadUserData() {
        Timber.d("loadUserData")

        val currentUser = appState.state?.currentUser

        if (currentUser == null) {
            // TODO add some error handling
            Timber.e("Null Current User")
            return
        }

        view?.let {
            it.setName(currentUser.name)
            it.setProfileImage(currentUser.avatarUri)
        }
    }

    override fun doLogout() {

    }
}