package dk.eboks.app.domain.interactors.ekey

import com.google.gson.JsonObject
import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class SetEKeyMasterkeyInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        SetEKeyMasterkeyInteractor {

    override var input: SetEKeyMasterkeyInteractor.Input? = null
    override var output: SetEKeyMasterkeyInteractor.Output? = null

    override fun execute() {
        try {
            val json = JsonObject()
            input?.let {
                json.addProperty("masterkey", it.masterKey)
                json.addProperty("masterkeyhash", it.masterKeyHash)

                val response = api.masterKeySet(json).execute()

                if (response?.isSuccessful == true) {
                    runOnUIThread { output?.onSetEKeyMasterkeySuccess(it.masterKeyUnencrypted) }
                }
            }.guard {
                throw InteractorException("wrong args")
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        runOnUIThread { output?.onSetEKeyMasterkeyError(viewError) }
    }
}