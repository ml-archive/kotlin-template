package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class SetEKeyVaultInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), SetEKeyVaultInteractor {

    override var input: SetEKeyVaultInteractor.Input? = null
    override var output: SetEKeyVaultInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val response = api.keyVaultSet(it.vault, it.signatureTime, it.signature).execute()

                when {
                    response?.isSuccessful == true -> runOnUIThread { output?.onSetEKeyVaultSuccess() }
                    response?.code() == 403 -> runOnUIThread { output?.onAuthError(it.retryCount) }
                    else -> throw InteractorException("Wrong input")
                }
            }.guard {
                throw InteractorException("Wrong input")
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        runOnUIThread { output?.onSetEKeyVaultError(viewError) }
    }
}