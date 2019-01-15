package dk.eboks.app.domain.interactors.shares

import dk.eboks.app.network.Api
import dk.eboks.app.util.errorBodyToViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class GetAllSharesInteractorImpl(executor: Executor, private val api: Api) :
    BaseInteractor(executor), GetAllSharesInteractor {
    override var output: GetAllSharesInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.getAllShares().execute()
            if (result.isSuccessful) {
                result.body()?.let {
                    runOnUIThread {
                        output?.onGetAllShares(it)
                    }
                }
            } else {
                runOnUIThread {
                    output?.onGetAllSharesError(errorBodyToViewError(result, false))
                }
            }
        } catch (t: Throwable) {
            Timber.e(t)
            runOnUIThread {
                output?.onGetAllSharesError(exceptionToViewError(t))
            }
        }
    }
}