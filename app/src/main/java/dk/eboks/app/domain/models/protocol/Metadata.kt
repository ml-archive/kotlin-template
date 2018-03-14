package dk.eboks.app.domain.models.protocol

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Metadata(
    var total : Int = 0,
    var unreadCount : Int = 0
) : Serializable