package dk.eboks.app.domain.interactors.sender.register

import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by Christian on 3/28/2018.
 * @author Christian
 * @since 3/28/2018.
 */
class GetRegistrationsInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor),
    GetRegistrationsInteractor {

    override var output: GetRegistrationsInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.getRegistrations().execute()
            runOnUIThread {
                if (result.isSuccessful) {
                    result?.body()?.let {
                        output?.onRegistrationsLoaded(it)
                    }
                } else {
                    output?.onError(
                        ViewError(
                            title = Translation.error.genericTitle,
                            message = Translation.error.genericMessage,
                            shouldCloseView = true
                        )
                    )
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
    }
}