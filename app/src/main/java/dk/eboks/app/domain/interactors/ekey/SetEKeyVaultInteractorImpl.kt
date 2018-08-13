package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class SetEKeyVaultInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        SetEKeyVaultInteractor {

    override var input: SetEKeyVaultInteractor.Input? = null
    override var output: SetEKeyVaultInteractor.Output? = null

    override fun execute() {
        try {
            val response = api.keyVaultSet().execute()

            if (response?.isSuccessful == true) {
                output?.onSetEKeyVaultSuccess()
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        output?.onSetEKeyVaultError(viewError)
    }
}