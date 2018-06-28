package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.login.AccessToken
import org.json.JSONObject

class AuthException(var httpCode : Int = -1) : RuntimeException("AuthException")

interface AuthClient {
    fun transformKspToken(kspToken : String, oauthToken : String? = null) : AccessToken?
    fun impersonate(token : String, userId : String)
    fun transformRefreshToken(refreshToken : String, longClient: Boolean = false) : AccessToken?
    fun login(username : String, password : String, activationCode : String?, longClient: Boolean = false, bearerToken : String? = null) : AccessToken?
    fun decodeJWTBody(JWTEncoded: String) : JSONObject
}