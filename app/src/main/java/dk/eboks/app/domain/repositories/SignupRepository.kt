package dk.eboks.app.domain.repositories

import com.google.gson.JsonObject


interface SignupRepository {
    fun verifySignupMail(email: String) : Boolean
    fun createUser(body: JsonObject): String
}