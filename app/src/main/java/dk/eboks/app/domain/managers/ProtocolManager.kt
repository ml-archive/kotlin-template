package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.request.UserInfo
import dk.eboks.app.network.rest.SessionData

/**
 * Created by bison on 08/12/17.
 */
interface ProtocolManager {
    val sessionData : SessionData
    val loggedIn : Boolean
    var userInfo : UserInfo

    fun init(deviceId : String)
    fun buildEboksHeader() : String
    fun generateChallenge(datetime : String) : String
    fun getDateTime(): String
    fun parseNonce(header : String) : String
    fun parseNonceAndSessionId(header : String) : Pair<String, String>
    fun generateResponseCode(): String
}