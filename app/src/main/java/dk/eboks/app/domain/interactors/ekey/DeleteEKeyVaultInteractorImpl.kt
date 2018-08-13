package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteEKeyVaultInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        DeleteEKeyVaultInteractor {

    override var output: DeleteEKeyVaultInteractor.Output? = null

    override fun execute() {
        try {
            val response = api.keyVaultDelete().execute()

            if (response?.isSuccessful == true) {
                output?.onDeleteEKeyVaultSuccess()
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        output?.onDeleteEKeyVaultError(viewError)
    }
}