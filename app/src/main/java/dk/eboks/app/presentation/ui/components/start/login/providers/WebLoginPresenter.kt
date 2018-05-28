package dk.eboks.app.presentation.ui.components.start.login.providers

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
open class WebLoginPresenter @Inject constructor(
        val appState: AppStateManager,
        val transformTokenInteractor: TransformTokenInteractor
) :
        WebLoginContract.Presenter,
        BasePresenterImpl<WebLoginContract.View>(),
        TransformTokenInteractor.Output {

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

    init {
        transformTokenInteractor.output = this
    }

    override fun setup() {
        appState.state?.loginState?.selectedUser?.let { user ->
            runAction { v -> v.setupLogin(user) }
        }

    }

    override fun cancelAndClose() {
        // set fallback login provider and close
        appState.state?.loginState?.selectedUser?.let { user ->
            user.lastLoginProvider?.let { provider_id ->
                Timber.e("Cancel and close called provider id = ")
                Config.getLoginProvider(provider_id)?.let { provider ->
                    Timber.e("Setting lastLoginProvider to fallback provider ${provider.fallbackProvider}")
                    user.lastLoginProvider = provider.fallbackProvider
                }
            }.guard {
                Timber.e("error")
            }
        }
        runAction { v -> v.close() }
    }

    override fun login(webToken: String) {
        appState.state?.loginState?.let{
            it.kspToken = webToken
            transformTokenInteractor.input = TransformTokenInteractor.Input(it)
            transformTokenInteractor.run()
        }
    }
}