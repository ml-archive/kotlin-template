package dk.eboks.app.mail.domain.interactors.shares

import dk.eboks.app.network.Api
import dk.eboks.app.network.util.errorBodyToViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

internal class GetAllSharesInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), GetAllSharesInteractor {
    override var output: GetAllSharesInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.getAllShares().execute()
            if (result.isSuccessful && result.body() != null) {
                result.body()?.let {
                    runOnUIThread {
                        output?.onGetAllShares(it)
                    }
                }
            } else {
                runOnUIThread {
                    output?.onGetAllSharesError(result.errorBodyToViewError(false))
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