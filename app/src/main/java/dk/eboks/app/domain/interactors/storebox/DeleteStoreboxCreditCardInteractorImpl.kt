package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class DeleteStoreboxCreditCardInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) :
    BaseInteractor(executor),
    DeleteStoreboxCreditCardInteractor {
    override var input: DeleteStoreboxCreditCardInteractor.Input? = null
    override var output: DeleteStoreboxCreditCardInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val result = api.deleteStoreboxCreditCard(it.id).execute()
                runOnUIThread {
                    output?.onDeleteCardSuccess(result.isSuccessful)
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onDeleteCardError(exceptionToViewError(t))
            }
        }
    }
}