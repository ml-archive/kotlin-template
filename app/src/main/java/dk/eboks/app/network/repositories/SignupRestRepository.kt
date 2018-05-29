package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.network.Api
import org.json.JSONObject

class SignupRestRepository(private val context: Context, private val api: Api, private val gson: Gson) : SignupRepository {

    override fun verifySignupMail(email : String): Boolean {
        val result = api.checkUserEmail(email).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return response?.body()?.exists ?: true
            }
            response.errorBody()?.string()?.let { error_str ->
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw(RuntimeException())
    }


    override fun createUser(body: JsonObject) {
        val result = api.createUserProfile(body).execute()
        result?.let { response ->
            if (response.isSuccessful) {

            }
            response.errorBody()?.string()?.let { error_str ->
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw(RuntimeException())
    }
}