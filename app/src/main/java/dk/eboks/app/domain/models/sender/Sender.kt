package dk.eboks.app.domain.models.sender

import java.io.Serializable
import dk.eboks.app.domain.models.Image

/**
 * Created by bison on 24-06-2017.
 */
data class Sender(
    var id : Long,
    var name : String = "",
    var unreadCount : Int = 0,
    var logo : Image? = null
) : Serializable