package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.network.Api

class SignupRestRepository(private val context: Context, private val api: Api, private val gson: Gson) : SignupRepository {

    override fun verifySignupMail(email : String): Boolean {
        val result = api.checkUserEmail(email).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return response?.body()?.exists ?: true
            }
        }
        throw(RuntimeException())
    }


    override fun createUser(body: JsonObject) {
        val result = api.createUserProfile(body).execute()
        if(result.isSuccessful)
            return
        throw(RuntimeException())
    }
}