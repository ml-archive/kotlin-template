package dk.eboks.app.keychain.presentation.components.verification

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.VerificationState
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class VerificationComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val appConfig: AppConfig
) :
    VerificationComponentContract.Presenter,
    BasePresenterImpl<VerificationComponentContract.View>() {

    init {
    }

    override fun setupVerificationState(signupVerification: Boolean) {
        appConfig.verificationProviderId?.let { provider_id ->
            appState.state?.verificationState = VerificationState(
                loginProviderId = provider_id,
                userBeingVerified = appState.state?.currentUser,
                kspToken = "",
                oldAccessToken = appState.state?.loginState?.token?.access_token,
                signupVerification = signupVerification
            )
        }
    }
}