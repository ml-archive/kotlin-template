package dk.eboks.app.domain.models.login

/**
 * Created by bison on 09-02-2018.
 */
data class LoginState(
        var firstLogin: Boolean = true,
        var lastUser: User? = null,
        var selectedUser: User? = null,
        var userName: String? = "3110276111",
        var userPassWord: String? = "147258369",
        var activationCode: String? = "Cr4x3N6Q",
        var kspToken: String? = null
)
