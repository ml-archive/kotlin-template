package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginPresenter
import dk.eboks.app.util.guard
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
class NemIdComponentPresenter @Inject constructor(appState: AppStateManager, transformTokenInteractor: TransformTokenInteractor) : WebLoginPresenter(appState, transformTokenInteractor) {
    /*
    override fun onLoginSuccess(response: AccessToken) {

        Timber.e("onLoginSuccess")
        // do we have a selected user
        appState.state?.loginState?.selectedUser?.let { user ->
            user.lastLoginProviderId = "nemid"
            appState.state?.currentUser = user
            appState.save()
        }.guard { // narp this is logging into an existing user

        }

        super.onLoginSuccess(response)
    }
    */
}