package dk.eboks.app.domain.models

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class Description(
        var text : String,
        var title : String? = null,
        var format : DescriptionFormat = DescriptionFormat.TEXT,
        var link: Link? = null
) : Serializable
