package dk.eboks.app.network.repositories

import com.google.gson.Gson
import com.google.gson.JsonObject
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.network.Api
import javax.inject.Inject

class UserRestRepository @Inject constructor(
    private val api: Api,
    private val gson: Gson
) : UserRepository {

    override fun updateProfile(user: JsonObject) {
        val result = api.updateProfile(user).execute()
        if (result.isSuccessful) {
            return
        }
        throw(RuntimeException())
    }

    override fun verifyEmail(mail: String) {
        val result = api.verifyEmail(mail).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return
            }
        }
        throw(RuntimeException())
    }

    override fun verifyPhone(number: String) {
        val result = api.verifyMobile(number).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return
            }
        }
        throw(RuntimeException())
    }

    override fun confirmPhone(number: String, code: String) {
        val result = api.confirmMobile(number, code).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return
            }
        }
        throw(RuntimeException())
    }

    override fun checkSsn(ssn: String): Boolean {
        val result = api.checkUserIdentity(ssn).execute()
        result.let { response ->
            if (response.isSuccessful) {
                return response.body()?.exists ?: true
            }
            response.errorBody()?.string()?.let { error_str ->
                throw(ServerErrorException(
                    gson.fromJson<ServerError>(
                        error_str,
                        ServerError::class.java
                    )
                ))
            }
        }
        throw(RuntimeException())
    }
}
