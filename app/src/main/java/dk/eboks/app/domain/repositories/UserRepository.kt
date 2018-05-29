package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.login.User

interface UserRepository {
    fun updateProfile(user: User)
    fun verifyEmail(mail: String)
    fun verifyPhone(number: String)

}