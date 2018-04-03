package dk.eboks.app.domain.models.shared

import dk.eboks.app.domain.models.Image
import java.io.Serializable
import java.util.*

/**
 * Created by thsk on 28/03/2018.
 */

data class Currency(
        var value : Double?,
        var curreny : String?
) : Serializable