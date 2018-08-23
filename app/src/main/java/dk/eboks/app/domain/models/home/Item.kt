package dk.eboks.app.domain.models.home

import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable
import java.util.*

/**
 * Created by thsk on 16/02/2018.
 */
data class Item(
        var id : String,
        var title : String?,
        var description: String?,
        var date: Date?,
        var amount: Double?,
        //var currency: String?,
        var status : Status?,
        var tag : String?,
        var image : Image?
) : Serializable