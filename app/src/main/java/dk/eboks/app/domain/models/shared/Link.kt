package dk.eboks.app.domain.models.shared

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class Link(
    var text : String? = null,
    var url : String = ""
) : Serializable