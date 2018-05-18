package dk.eboks.app.domain.interactors.authentication

import com.google.gson.Gson
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.injection.modules.RestModule
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class LoginInteractorImpl(executor: Executor, val api: Api, val appStateManager: AppStateManager) : BaseInteractor(executor), LoginInteractor {
    override var output : LoginInteractor.Output? = null
    override var input : LoginInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                var map = mapOf(
                        Pair("grant_type", "password"),
                        Pair("username", it.username),
                        Pair("password", it.password),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_id", "MobileApp-Long-id"),
                        Pair("client_secret", "MobileApp-Long-secret")
                )

                if(it.activationCode != null)
                {
                    map = map.plus(Pair("acr_values", "activationcode:${it.activationCode} nationality:DK"))
                }

                val result = api.getToken(map).execute()
                runOnUIThread {
                    if (result.isSuccessful) {
                        result?.body()?.let { token ->
                            appStateManager.state?.loginState?.token = token
                            appStateManager.save()
                            output?.onLoginSuccess(token)
                        }
                    } else {
                        output?.onLoginDenied(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onLoginError(exceptionToViewError(t))
            }
        }
    }
}