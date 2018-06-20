package dk.eboks.app.domain.models.login

/**
 * Created by bison on 09-02-2018.
 */
data class LoginState(
        var firstLogin: Boolean = true,
        var lastUser: User? = null,
        var selectedUser: User? = null,

        // These values are temporary
        var userName: String? = null,
        var userPassWord: String? = null,
        var userLoginProviderId: String? = null,

        var activationCode: String? = null,
        var kspToken: String? = null,
        var token: AccessToken? = null
) {
    override fun toString(): String {
        return selectedUser?.name ?: super.toString()
    }
}
