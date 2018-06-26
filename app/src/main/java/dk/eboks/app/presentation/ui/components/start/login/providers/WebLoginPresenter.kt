package dk.eboks.app.presentation.ui.components.start.login.providers

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.util.guard
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
open class WebLoginPresenter @Inject constructor(
        val appState: AppStateManager,
        val transformTokenInteractor: TransformTokenInteractor,
        val verifyProfileInteractor: VerifyProfileInteractor

) :
        WebLoginContract.Presenter,
        BasePresenterImpl<WebLoginContract.View>(),
        TransformTokenInteractor.Output,
        VerifyProfileInteractor.Output
{


    init {
        transformTokenInteractor.output = this
        verifyProfileInteractor.output = this
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
                appState.state?.loginState?.userLoginProviderId = provider.fallbackProvider
            }.guard {
                Timber.e("error")
            }
        }
        runAction { v -> v.close() }
    }

    /**
     * yo B, 'tis be where the ksp token is handed of to, peace
     * We have to deal with either a login, verification and probably some additional shit
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

    override fun onSuccess(response: AccessToken) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showError(error)
        }
    }
}