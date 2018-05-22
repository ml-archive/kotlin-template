package dk.eboks.app.domain.models.login

/**
 * Created by bison on 09-02-2018.
 */
data class LoginState(
        var firstLogin: Boolean = true,
        var lastUser: User? = null,
        var selectedUser: User? = null,
        var userName: String? = "3110276111", // Todo remove default value
        var userPassWord: String? = "147258369", // Todo remove default value
        var activationCode: String? = "Cr4x3N6Q", // Todo remove default value
        var kspToken: String? = null,
        var token: AccessToken? = null
)
