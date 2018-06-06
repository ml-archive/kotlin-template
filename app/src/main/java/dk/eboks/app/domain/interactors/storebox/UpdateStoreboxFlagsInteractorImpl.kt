package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class UpdateStoreboxFlagsInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        UpdateStoreboxFlagsInteractor {
    override var input: UpdateStoreboxFlagsInteractor.Input? = null
    override var output: UpdateStoreboxFlagsInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val result = api.updateChannelFlags(it.flags).execute()

                runOnUIThread {
                    output?.onUpdateFlagsSuccess()
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onUpdateFlagsError(exceptionToViewError(t))
            }
        }
    }
}