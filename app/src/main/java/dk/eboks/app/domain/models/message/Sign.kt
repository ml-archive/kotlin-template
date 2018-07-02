package dk.eboks.app.domain.models.message

import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Sign(
        var status : Status,
        var reject : Int?

) : Serializable