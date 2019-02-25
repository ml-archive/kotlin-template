package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class DeleteStoreboxReceiptInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), DeleteStoreboxReceiptInteractor {
    override var input: DeleteStoreboxReceiptInteractor.Input? = null
    override var output: DeleteStoreboxReceiptInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val result = api.deleteStoreboxReceipt(it.id).execute()
                if (result.isSuccessful) {
                    runOnUIThread {
                        output?.onDeleteReceiptSuccess()
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onDeleteReceiptError(exceptionToViewError(t))
            }
        }
    }
}