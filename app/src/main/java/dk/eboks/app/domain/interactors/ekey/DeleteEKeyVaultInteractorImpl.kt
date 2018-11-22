package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteEKeyVaultInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        DeleteEKeyVaultInteractor {

    override var output: DeleteEKeyVaultInteractor.Output? = null
    override var input: DeleteEKeyVaultInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                val response = api.keyVaultDelete(it.signatureTime, it.signature).execute()

                if (response?.isSuccessful == true) {
                    runOnUIThread { output?.onDeleteEKeyVaultSuccess() }
                }
            }.guard {
                throw Exception("Wrong input")
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        runOnUIThread { output?.onDeleteEKeyVaultError(viewError) }
    }
}