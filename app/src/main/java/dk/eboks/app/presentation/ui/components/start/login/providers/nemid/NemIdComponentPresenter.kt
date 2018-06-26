package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
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
class NemIdComponentPresenter @Inject constructor(appState: AppStateManager, transformTokenInteractor: TransformTokenInteractor, verifyProfileInteractor: VerifyProfileInteractor) : WebLoginPresenter(appState, transformTokenInteractor, verifyProfileInteractor) {

    override fun login(webToken: String) {
        appState.state?.loginState?.let {
            it.userLoginProviderId = "nemid"
        }
        super.login(webToken)
    }
}