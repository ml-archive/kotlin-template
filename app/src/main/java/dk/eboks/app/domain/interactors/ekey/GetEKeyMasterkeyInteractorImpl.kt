package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class GetEKeyMasterkeyInteractorImpl(executor: Executor, private val api: Api) :
    BaseInteractor(executor),
    GetEKeyMasterkeyInteractor {

    override var output: GetEKeyMasterkeyInteractor.Output? = null
    override var input: GetEKeyMasterkeyInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                val response = api.masterKeyGet().execute()

                when {
                    response?.isSuccessful == true -> runOnUIThread {
                        output?.onGetEKeyMasterkeySuccess(
                            response.body()?.masterkey
                        )
                    }
                    response?.code() == 403 -> runOnUIThread { output?.onAuthError(it.isRetry) }
                    response?.code() == 404 -> runOnUIThread { output?.onGetEkeyMasterkeyNotFound() }
                    else -> throw InteractorException(
                        "Error in response: ${response.code()}",
                        response.code()
                    )
                }
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        runOnUIThread { output?.onGetEKeyMasterkeyError(viewError) }
    }
}