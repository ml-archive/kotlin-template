package dk.eboks.app.domain.interactors.ekey

import com.google.gson.JsonObject
import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import org.json.JSONObject

class GetEKeyMasterkeyInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        GetEKeyMasterkeyInteractor {

    override var output: GetEKeyMasterkeyInteractor.Output? = null

    override fun execute() {
        try {
            val response = api.masterKeyGet().execute()

            if (response?.isSuccessful == true) {
                output?.onGetEKeyMasterkeySuccess(response.body()!!)
            } else if(response?.code() == 404) {
                output?.onGetEkeyMasterkeyNotFound()
            } else {
                throw InteractorException("Error in response: ${response.code()}", response.code())
            }
        } catch (exception: Exception) {
            showViewException(exception)
            return
        }
    }

    private fun showViewException(exception: Exception) {
        val viewError = exceptionToViewError(exception)
        output?.onGetEKeyMasterkeyError(viewError)
    }
}