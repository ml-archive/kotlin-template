package dk.eboks.app.presentation.ui.components.start.login.providers.bankidno

import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginPresenter
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class BankIdNOComponentPresenter @Inject constructor(appState: AppStateManager, transformTokenInteractor: TransformTokenInteractor) : WebLoginPresenter(appState, transformTokenInteractor) {

    override fun login(webToken: String) {
        appState.state?.loginState?.let {
            it.userLoginProviderId = "bankid_no"
        }
        super.login(webToken)
    }
}