package dk.eboks.app.domain.models

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class Attachment(
    var id : Long = 0,
    var name : String = "",
    var size : String = "",
    var format : String = "",
    var encoding : String = ""
) : Serializable