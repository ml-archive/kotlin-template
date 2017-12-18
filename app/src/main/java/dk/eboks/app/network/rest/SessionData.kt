package dk.eboks.app.network.rest

import java.io.Serializable

/**
 * Created by bison on 07/12/17.
 */
data class SessionData (
        var sessionId : String,
        var nonce : String? = null,
        var responseCode : String,
        var userId : String,
        var ticketType : String,
        var ticket : String
) : Serializable
