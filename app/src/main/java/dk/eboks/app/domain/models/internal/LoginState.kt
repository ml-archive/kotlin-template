package dk.eboks.app.domain.models.internal

/**
 * Created by bison on 09-02-2018.
 */
data class LoginState(
        var firstLogin : Boolean = true,
        var lastUser: User? = null,
        var selectedUser: User? = null
)
