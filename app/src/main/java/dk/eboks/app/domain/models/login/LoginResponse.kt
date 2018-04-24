package dk.eboks.app.domain.models.login

/**
 *
 * Created by Christian on 4/20/2018.
 * @author   Christian
 * @since    4/20/2018.
 */
data class LoginResponse(
        var access_token: String,
        var expires_in: Int,
        var token_type: String,
        var refresh_token: String
) {
    override fun toString() : String {
        return "Type: $token_type \nToken: $access_token \nRefresh: $refresh_token"
    }
}