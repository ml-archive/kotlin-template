package dk.eboks.app.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Image(
    var url : String,
    var text : String? = null,
    var version : String? = null
) : Serializable