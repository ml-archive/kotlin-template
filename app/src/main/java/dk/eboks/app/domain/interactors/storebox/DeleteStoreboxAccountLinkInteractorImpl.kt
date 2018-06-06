package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteStoreboxAccountLinkInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        DeleteStoreboxAccountLinkInteractor {
    override var output: DeleteStoreboxAccountLinkInteractor.Output? = null

    override fun execute() {
        try {
            // TODO remove simulation

            Thread.sleep(5000)
            runOnUIThread {
                output?.onStoreboxAccountLinkDelete()
            }
            /*
            val result = api.deleteStoreboxAccountLink().execute()

            if(result.isSuccessful) {
                runOnUIThread {
                    output?.onStoreboxAccountLinkDelete()
                }
            }
            */
        } catch (t: Throwable) {
            t.printStackTrace()
            runOnUIThread {
                output?.onStoreboxAccountLinkDeleteError(exceptionToViewError(t))
            }
        }
    }
}