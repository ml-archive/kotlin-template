package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteEKeyMasterkeyInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        DeleteEKeyMasterkeyInteractor {

    override var output: DeleteEKeyMasterkeyInteractor.Output? = null

    override fun execute() {
        try {
            val response = api.masterKeyDelete().execute()

            if (response?.isSuccessful == true) {
                runOnUIThread { output?.onDeleteEKeyMasterkeySuccess() }
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        runOnUIThread { output?.onDeleteEKeyMasterkeyError(viewError) }
    }
}