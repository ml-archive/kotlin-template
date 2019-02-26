package dk.eboks.app.presentation.ui.login.components.providers.nemid

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.keychain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.keychain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.keychain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import cz.levinzonr.keychain.presentation.components.providers.WebLoginPresenter
import javax.inject.Inject

/**
 * Created by Christian on 5/28/2018.
 * @author Christian
 * @since 5/28/2018.
 */
class NemIdComponentPresenter @Inject constructor(
    appState: AppStateManager,
    transformTokenInteractor: TransformTokenInteractor,
    verifyProfileInteractor: VerifyProfileInteractor,
    mergeAndImpersonateInteractor: MergeAndImpersonateInteractor,
    userSettingsManager: UserSettingsManager,
    appConfig: AppConfig
) :
    WebLoginPresenter(
        appState,
        transformTokenInteractor,
        verifyProfileInteractor,
        mergeAndImpersonateInteractor,
        userSettingsManager,
        appConfig
    ) {

    override fun login(webToken: String) {
        appState.state?.loginState?.let {
            it.userLoginProviderId = "nemid"
        }
        super.login(webToken)
    }
}