package dk.eboks.app.network.repositories

import dk.eboks.app.domain.repositories.UserRepository
import android.content.Context
import com.google.gson.Gson
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.network.Api

class UserRestRepository(private val context: Context, private val api: Api, private val gson: Gson) : UserRepository {

    override fun updateProfile(user: User) {
        val result = api.updateProfile(user).execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return
            }
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

    override fun verifyPhone(number: String){

    }
}
