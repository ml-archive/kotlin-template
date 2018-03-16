package dk.eboks.app.domain.models.channel

import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.domain.models.shared.Description
import java.io.Serializable

/**
 * Created by thsk on 16/02/2018.
 */
data class Channel(
        var id : Int,
        var name : String,
        var payoff : String,
        var description: Description?,
        var status: Status?,
        var logo : Image?,
        var image : Image?,
        var background : ChannelColor?,
        var requirements : Array<Requirement>? = null,
        var installed : Boolean,
        var pinned : Boolean?
) : Serializable