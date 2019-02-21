package dk.eboks.app.network.repositories

import com.google.gson.JsonObject
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.network.Api
import javax.inject.Inject

// TODO delete me, create user moved to interactor
class SignupRestRepository @Inject constructor(private val api: Api) :
    SignupRepository {

    override fun verifySignupMail(email: String): Boolean {
        val response = api.checkUserEmail(email).execute()
        if (response.isSuccessful) {
            return response.body()?.exists ?: true
        }
        throw(RuntimeException())
    }

    override fun createUser(body: JsonObject) {
        val result = api.createUserProfile(body).execute()
        if (result.isSuccessful) return
        throw(RuntimeException())
    }
}