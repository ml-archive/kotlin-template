package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.JsonObject
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.guard


// TODO delete me, create user moved to interactor
class SignupRestRepository(private val context: Context, private val api: Api) : SignupRepository {

    override fun verifySignupMail(email : String): Boolean {
        val result = api.checkUserEmail(email).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return response.body()?.exists ?: true
            }
        }
        throw(RuntimeException())
    }


    override fun createUser(body: JsonObject)  {
        val result = api.createUserProfile(body).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return
            }
        }.guard {
            throw(RuntimeException())

        }
        throw(RuntimeException())
    }
}