package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.network.Api
import dk.eboks.app.network.util.errorBodyToViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/29/2018.
 * @author Christian
 * @since 5/29/2018.
 */
class ResetPasswordInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api,
    private val appConfig: AppConfig
) :
    BaseInteractor(executor),
    ResetPasswordInteractor {
    override var input: ResetPasswordInteractor.Input? = null
    override var output: ResetPasswordInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val response =
                    api.resetPassword(appConfig.currentMode.countryCode, it.email).execute()
                runOnUIThread {
                    if (response.isSuccessful) {
                        Timber.i("Successfully Reset password")
                        output?.onSuccess()
                    } else {
                        output?.onError(response.errorBodyToViewError())
                    }
                }
            }
        } catch (t: Throwable) {
            Timber.e("Reset password fail: $t")
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
    }
}