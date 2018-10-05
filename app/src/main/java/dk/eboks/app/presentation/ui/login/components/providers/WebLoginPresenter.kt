package dk.eboks.app.presentation.ui.login.components.providers

import android.app.Activity
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.util.ViewControl
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
open class WebLoginPresenter @Inject constructor(
        val appState: AppStateManager,
        val transformTokenInteractor: TransformTokenInteractor,
        val verifyProfileInteractor: VerifyProfileInteractor,
        val mergeAndImpersonateInteractor: MergeAndImpersonateInteractor,
        val userSettingsManager: UserSettingsManager

        ) :
        WebLoginContract.Presenter,
        BasePresenterImpl<WebLoginContract.View>(),
        TransformTokenInteractor.Output,
        VerifyProfileInteractor.Output,
        MergeAndImpersonateInteractor.Output
{


    init {
        transformTokenInteractor.output = this
        verifyProfileInteractor.output = this
        mergeAndImpersonateInteractor.output = this
    }

    companion object {
        var newIdentity: String? = null
    }

    override fun setup() {
//        appState.state?.loginState?.userLoginProviderId = "nemid"
        appState.state?.loginState?.selectedUser?.let { user ->
            runAction { v -> v.setupLogin(user) }
        }.guard {
            // narp this is a first time login using the provider
            runAction { v -> v.setupLogin(null) }
        }

    }

    override fun cancelAndClose() {
        // set fallback login provider and close
        val failProviderId = appState.state?.loginState?.userLoginProviderId
        failProviderId?.let {
            Timber.e("Cancel and close called provider id = ${it}")
            Config.getLoginProvider(failProviderId)?.let { provider ->
                Timber.e("Setting lastLoginProvider to fallback provider ${provider.fallbackProvider}")
                userSettingsManager.get(appState.state?.loginState?.selectedUser?.id ?: -1)?.let { userSettings ->
                    userSettings.lastLoginProviderId = provider.fallbackProvider
                    userSettingsManager.put(userSettings)
                    userSettingsManager.save()
                }
                //appState.state?.loginState?.userLoginProviderId = provider.fallbackProvider

            }.guard {
                Timber.e("error")
            }
        }
        runAction { v -> v.close() }
    }

    /**
     * yo B, 'tis be where the ksp token is handed of to, peace
     * We have to deal with either a login, verification and probably some additional stuff
     */
    override fun login(webToken: String) {
        // Do we have a verification state
        appState.state?.verificationState?.let { verificationState ->
            Timber.e("We're verifying, not logging in")
            verificationState.kspToken = webToken
            verifyProfileInteractor.input = VerifyProfileInteractor.Input(verificationState)
            verifyProfileInteractor.run()
        }.guard {   // narp, maybe we'd be loggin in?
            appState.state?.loginState?.let {
                Timber.e("We're logging in, not verifying")
                it.kspToken = webToken
                transformTokenInteractor.input = TransformTokenInteractor.Input(it)
                transformTokenInteractor.run()
            }
        }
    }

    // the is called after the users is presented with the merge account drawer, check the result and merge or not
    override fun mergeAccountOrKeepSeparated() {
        Timber.e("Merge account or keep separated")
        appState.state?.verificationState?.let { state->
            mergeAndImpersonateInteractor.input = MergeAndImpersonateInteractor.Input(state)
            mergeAndImpersonateInteractor.run()
        }
    }

    /**
     * TransformTokenInteractor callbacks
     */
    override fun onLoginSuccess(response: AccessToken) {
        runAction { v ->
            v.proceed()
        }
    }

    override fun onLoginError(error: ViewError) {
        runAction { v ->
            v.showError(error)
        }
    }

    /**
     * VerifyProfileInteractor callbacks
     */
    override fun onVerificationSuccess(new_identity : String?) {
        Timber.e("VerificationSuccess")
        Timber.e("Got new identity back after verification: $new_identity")
        ViewControl.refreshAllOnResume()
        newIdentity = new_identity
        runAction { v -> v.finishActivity(Activity.RESULT_OK) }
    }

    override fun onVerificationError(error: ViewError) {
        runAction { v ->
            error.shouldCloseView = true
            v.showErrorDialog(error)
        }
    }

    override fun onAlreadyVerifiedProfile() {
        runAction { v->v.showMergeAcountDrawer() }
    }

    /**
     * MergeAndImpersonateInteractor callbacks
     */

    override fun onMergeCompleted() {
        ViewControl.refreshAllOnResume()
        runAction { v -> v.finishActivity(Activity.RESULT_OK) }
    }

    override fun onMergeError(error: ViewError) {
        runAction { v->
            v.showError(error)
        }
    }
}