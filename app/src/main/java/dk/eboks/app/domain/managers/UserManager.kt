package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.login.User

/**
 * Created by bison on 18-02-2018.
 */
interface UserManager {
    var users : MutableList<User>
    fun add(user : User)
    fun remove(user : User)
    fun save(user : User)
}