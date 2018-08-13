package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class GetEKeyVaultInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        GetEKeyVaultInteractor {

    override var output: GetEKeyVaultInteractor.Output? = null

    override fun execute() {
        try {
            val response = api.keyVaultGet().execute()

            if (response?.isSuccessful == true) {
                output?.onGetEKeyVaultSuccess()
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        output?.onGetEKeyVaultError(viewError)
    }
}