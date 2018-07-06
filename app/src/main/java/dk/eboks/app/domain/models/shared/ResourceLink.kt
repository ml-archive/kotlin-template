package dk.eboks.app.domain.models.shared

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class ResourceLink(
    var type : String,
    var link : Link
) : Serializable