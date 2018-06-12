package dk.eboks.app.presentation.ui.components.profile.drawer

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

class FingerPrintComponentPresenter @Inject constructor(val appState: AppStateManager) :
        FingerPrintComponentContract.Presenter,
        BasePresenterImpl<FingerPrintComponentContract.View>() {

    override fun onViewCreated(view: FingerPrintComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        loadUserState()
    }

    private fun loadUserState() {
        val lastLoginProvider: String? = appState.state?.currentUser?.lastLoginProviderId

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
        val errorMessage = ViewError(Translation.error.genericTitle, Translation.error.genericMessage, true, true)
        view?.showErrorDialog(errorMessage)
    }
}