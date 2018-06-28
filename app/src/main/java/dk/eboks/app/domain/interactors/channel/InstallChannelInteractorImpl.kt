package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class InstallChannelInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor), InstallChannelInteractor {
    override var output: InstallChannelInteractor.Output? = null
    override var input: InstallChannelInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args->
                val result = api.installChannel(args.id).execute()
                if(result.isSuccessful) {
                    runOnUIThread {
                        output?.onInstallChannel()
                    }
                }
                else
                {
                    runOnUIThread {
                        output?.onInstallChannelError(ViewError())
                    }
                }
            }.guard {
                throw(InteractorException("bad args"))
            }
        } catch (t: Throwable) {
            runOnUIThread {
                val ve = exceptionToViewError(t)
                output?.onInstallChannelError(ve)
            }
        }
    }
}