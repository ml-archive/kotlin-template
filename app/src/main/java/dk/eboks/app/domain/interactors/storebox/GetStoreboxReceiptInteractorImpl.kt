package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.network.util.errorBodyToViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

class GetStoreboxReceiptInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), GetStoreboxReceiptInteractor {
    override var input: GetStoreboxReceiptInteractor.Input? = null
    override var output: GetStoreboxReceiptInteractor.Output? = null

    override fun execute() {
        Timber.d("execute")

        try {
            val storeboxId = input?.storeboxId ?: return

            val response = api.getStoreboxReceipt(storeboxId).execute()

            if (response.isSuccessful) {
                Timber.d("Successfully Loaded Receipt")
                runOnUIThread {
                    response.body()?.let {
                        output?.onGetReceipt(it)
                    }
                }
            } else {
                Timber.e("Error Loading Receipt: isSuccessful -> False")
                runOnUIThread {
                    output?.onGetReceiptsError(response.errorBodyToViewError())
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error Loading Receipt")
            e.printStackTrace()

            runOnUIThread {
                output?.onGetReceiptsError(exceptionToViewError(e, true))
            }
        }
    }
}