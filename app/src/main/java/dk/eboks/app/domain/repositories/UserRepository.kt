package dk.eboks.app.domain.repositories

import com.google.gson.JsonObject

interface UserRepository {
    fun updateProfile(user: JsonObject)
    fun verifyEmail(mail: String)
    fun verifyPhone(number: String)
    fun checkSsn(ssn: String): Boolean

}