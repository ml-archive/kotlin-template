package dk.eboks.app.presentation.ui.components.start.login.providers.bankidse

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginPresenter
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class BankIdSEComponentPresenter @Inject constructor(appState: AppStateManager, transformTokenInteractor: TransformTokenInteractor) : WebLoginPresenter(appState, transformTokenInteractor) {

    override fun login(webToken: String) {
        appState.state?.loginState?.let {
            it.userLoginProviderId = "bankid_se"
        }
        super.login(webToken)
    }
}