package dk.eboks.app.presentation.ui.components.verification

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.VerificationState
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class VerificationComponentPresenter @Inject constructor(val appState: AppStateManager) :
        VerificationComponentContract.Presenter,
        BasePresenterImpl<VerificationComponentContract.View>()
{

    init {
    }

    override fun setupVerificationState() {
        Config.getVerificationProviderId()?.let { provider_id ->
            appState.state?.verificationState = VerificationState(provider_id, appState.state?.currentUser, "", appState.state?.loginState?.token?.access_token)
        }
    }
}