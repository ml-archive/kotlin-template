package dk.eboks.app.domain.models.login

/**
 * Created by bison on 09-02-2018.
 */
data class LoginState(
        var firstLogin: Boolean = true,
        var lastUser: User? = null,
        var selectedUser: User? = null,

        // These values are temporary
        var userName: String? = null,// = "3110276111", // Todo remove default value
        var userPassWord: String? = null,// = "147258369", // Todo remove default value
        var userLoginProviderId: String? = null,

        var activationCode: String? = null,// "Cr4x3N6Q", // Todo remove default value
        var kspToken: String? = null,
        var token: AccessToken? = null
) {
    override fun toString(): String {
        return selectedUser?.name ?: super.toString()
    }
}
