package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.network.Api
import dk.eboks.app.util.errorBodyToViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by Christian on 5/29/2018.
 * @author   Christian
 * @since    5/29/2018.
 */
class ResetPasswordInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor), ResetPasswordInteractor {
    override var input: ResetPasswordInteractor.Input? = null
    override var output: ResetPasswordInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val response = api.resetPassword(Config.currentMode.countryCode, it.email).execute()
                runOnUIThread {
                    if (response.isSuccessful) {
                        Timber.i("Successfully Reset password")
                        output?.onSuccess()
                    }
                    else {
                        output?.onError(errorBodyToViewError(response))
                    }
                }
            }
        } catch (t: Throwable) {
            Timber.e("Reset password fail: $t")
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
        runOnUIThread {
            output?.onError(ViewError())
        }
    }
}