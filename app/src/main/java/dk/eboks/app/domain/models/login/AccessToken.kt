package dk.eboks.app.domain.models.login

import java.io.Serializable

/**
 *
 * Created by Christian on 4/20/2018.
 * @author   Christian
 * @since    4/20/2018.
 */
data class AccessToken(
        var access_token: String,
        var expires_in: Int,
        var token_type: String,
        var refresh_token: String,
        var scope: String? = "mobileapi offline_access",
        var client_id: String? = "simplelogin",
        var client_secret: String? = "2BB80D537B1DA3E38BD30361AA855686BDE0EACD7162FEF6A25FE97BF527A25B"
) : Serializable {
    override fun toString() : String {
        return "Type: $token_type \nToken: $access_token \nRefresh: $refresh_token"
    }
}