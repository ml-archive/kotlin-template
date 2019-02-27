package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by bison on 01/02/18.
 */
internal class UninstallChannelInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), UninstallChannelInteractor {
    override var output: UninstallChannelInteractor.Output? = null
    override var input: UninstallChannelInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                val result = api.uninstallChannel(args.id).execute()
                if (result.isSuccessful) {
                    runOnUIThread {
                        output?.onUninstallChannel()
                    }
                } else {
                    runOnUIThread {
                        output?.onUninstallChannelError(ViewError())
                    }
                }
            }.guard {
                throw(InteractorException("bad args"))
            }
        } catch (t: Throwable) {
            runOnUIThread {
                val ve = exceptionToViewError(t)
                output?.onUninstallChannelError(ve)
            }
        }
    }
}