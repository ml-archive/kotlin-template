package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.errorBodyToViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class GetStoreboxReceiptsInteractorImpl(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor),
    GetStoreboxReceiptsInteractor {
    override var output: GetStoreboxReceiptsInteractor.Output? = null

    override fun execute() {
        Timber.d("execute")

        try {
            val response = api.getStoreboxReceipts().execute()

            if (response.isSuccessful) {
                Timber.d("Successfully Loaded Receipts")
                runOnUIThread {
                    response.body()?.let {
                        output?.onGetReceipts(it)
                    }
                }
            } else {
                Timber.e("Error Loading Receipts")
                output?.onGetReceiptsError(errorBodyToViewError(response))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error Loading Receipts")
            output?.onGetReceiptsError(exceptionToViewError(e, true))
        }
    }
}