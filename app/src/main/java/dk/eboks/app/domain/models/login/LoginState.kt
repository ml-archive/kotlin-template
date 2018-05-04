package dk.eboks.app.domain.models.login

/**
 * Created by bison on 09-02-2018.
 */
data class LoginState(
        var firstLogin: Boolean = true,
        var lastUser: User? = null,
        var selectedUser: User? = null,
        var userName: String? = null,
        var userPassWord: String? = null,
        var activationCode: String? = null,
        var kspToken: String? = null
)
