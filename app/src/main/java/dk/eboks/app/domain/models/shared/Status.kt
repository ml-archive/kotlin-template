package dk.eboks.app.domain.models.shared

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class Status(
    var important : Boolean = false,
    var title : String? = null,
    var text : String? = null,
    var type : Int? = 0,
    var date : Date
) : Serializable