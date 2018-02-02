package dk.eboks.app.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Status(
    var important : Boolean = false,
    var text : String = "",
    var type : Int = 0
) : Serializable