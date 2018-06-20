package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.login.AccessToken

class AuthException(var httpCode : Int = -1) : RuntimeException("AuthException")

interface AuthClient {
    fun transformKspToken(kspToken : String) : AccessToken?
    fun transformRefreshToken(refreshToken : String, longClient: Boolean = false) : AccessToken?
    fun login(username : String, password : String, activationCode : String?, longClient: Boolean = false) : AccessToken?
}