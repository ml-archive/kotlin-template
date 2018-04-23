package dk.eboks.app.domain.models.login

/**
 *
 * Created by Christian on 4/20/2018.
 * @author   Christian
 * @since    4/20/2018.
 */
data class LoginResponse(
        var accessToken: String,
        var expiresIn: Int,
        var tokenType: String,
        var refreshToken: String
)