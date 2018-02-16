package dk.eboks.app.network.managers.protocol

import java.io.Serializable

/**
 * Created by bison on 07/12/17.
 */
data class SessionData (
        var initialized : Boolean = false,
        var deviceId : String = "",
        var sessionId : String = "",
        var nonce : String? = null,
        var responseCode : String = "",
        var userId : String = "",
        var ticketType : String = "",
        var ticket : String = ""
) : Serializable
