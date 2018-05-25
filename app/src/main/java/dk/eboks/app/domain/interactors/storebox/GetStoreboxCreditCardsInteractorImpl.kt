package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class GetStoreboxCreditCardsInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        GetStoreboxCreditCardsInteractor {
    override var output: GetStoreboxCreditCardsInteractor.Output? = null

    override fun execute() {
        Timber.d("executeexecuteexecuteexecute")
        try {
            val result = api.getStoreboxCreditCards().execute()

            result.body()?.let {
                runOnUIThread {
                    output?.onGetCardsSuccessful(it)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()

            runOnUIThread {
                output?.onGetCardsError(exceptionToViewError(t))
            }
        }
    }
}