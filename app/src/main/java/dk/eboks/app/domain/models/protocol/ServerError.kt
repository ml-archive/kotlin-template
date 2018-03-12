package dk.eboks.app.domain.models.protocol

import dk.eboks.app.domain.models.shared.Description
import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class ServerError(
        var id : String,
        var type : ErrorType,
        var code : Int = -1,
        var description: Description?,
        var debug : String
) : Serializable