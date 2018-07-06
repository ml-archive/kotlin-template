package dk.eboks.app.presentation.ui.login.components.providers.idporten

import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.presentation.ui.login.components.providers.WebLoginPresenter
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class IdPortenComponentPresenter @Inject constructor(
        appState: AppStateManager,
        transformTokenInteractor: TransformTokenInteractor,
        verifyProfileInteractor: VerifyProfileInteractor,
        mergeAndImpersonateInteractor: MergeAndImpersonateInteractor,
        userSettingsManager: UserSettingsManager) :
        WebLoginPresenter(appState, transformTokenInteractor, verifyProfileInteractor, mergeAndImpersonateInteractor, userSettingsManager) {

    override fun login(webToken: String) {
        appState.state?.loginState?.let {
            it.userLoginProviderId = "idporten"
        }
        super.login(webToken)
    }
}