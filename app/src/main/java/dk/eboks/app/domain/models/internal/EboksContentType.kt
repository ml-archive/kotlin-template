package dk.eboks.app.domain.models.internal

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class EboksContentType(
    var fileExtension : String,
    var mimeType : String? = null
) : Serializable