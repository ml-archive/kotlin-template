package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
class TransformTokenInteractorImpl(executor: Executor, val api: Api, val appStateManager: AppStateManager) : BaseInteractor(executor), TransformTokenInteractor {
    override var output: TransformTokenInteractor.Output? = null
    override var input: TransformTokenInteractor.Input? = null

    override fun execute() {
        try {
            input?.loginState?.kspToken?.let {
                val result = api.getToken(mapOf(
                        Pair("token", it),
                        Pair("grant_type", "kspwebtoken"),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_id", BuildConfig.OAUTH_LONG_ID),
                        Pair("client_secret", BuildConfig.OAUTH_LONG_SECRET)
                )).execute()

                input?.loginState?.kspToken = null // consume the token - it's only usable once anyway

                runOnUIThread {
                    if (result.isSuccessful) {
                        result?.body()?.let { token ->
                            appStateManager.state?.loginState?.token = token
                            appStateManager.save()
                            output?.onLoginSuccess(token)
                        }
                    } else {
                        output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                    }
                }
            }
        } catch (t: Throwable) {
            Timber.e("Token transform fail: $t")
            runOnUIThread {
                output?.onLoginError(exceptionToViewError(t))
            }
        }
    }
}