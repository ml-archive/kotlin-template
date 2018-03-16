package dk.eboks.app.domain.models.protocol

import java.io.Serializable

data class Metadata(
    var total : Int = 0,
    var unreadCount : Int = 0
) : Serializable