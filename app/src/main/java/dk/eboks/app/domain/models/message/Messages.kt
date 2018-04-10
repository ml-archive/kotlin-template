package dk.eboks.app.domain.models.message

import dk.eboks.app.domain.models.protocol.Metadata
import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Messages(
    var items: List<Message>,
    var metadata: Metadata? = null
) : Serializable