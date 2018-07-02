package dk.eboks.app.presentation.ui.components.verification

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
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

    override fun setupVerificationState(signupVerification : Boolean) {
        Config.getVerificationProviderId()?.let { provider_id ->
            appState.state?.verificationState = VerificationState(
                    loginProviderId = provider_id,
                    userBeingVerified = appState.state?.currentUser,
                    kspToken = "",
                    oldAccessToken = appState.state?.loginState?.token?.access_token,
                    signupVerification = signupVerification)
        }
    }
}