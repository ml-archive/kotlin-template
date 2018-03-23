package dk.eboks.app.domain.models.protocol

import dk.eboks.app.domain.models.shared.Description
import java.io.Serializable

data class ServerError(
        var id : String,
        var type : ErrorType,
        var code : Int = -1,
        var description: Description? = null,
        var debug : String? = null
) : Serializable