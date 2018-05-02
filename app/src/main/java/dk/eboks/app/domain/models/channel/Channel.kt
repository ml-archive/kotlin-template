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
        var description: Description?,  // this is removed in swagger 17. april (Been readded in the latest)
        var status: Status?,
        var logo : Image?,
        var image : Image?,
        var background : ChannelColor?,   // this is changed in swagger 17. april  now a string
        var requirements : Array<Requirement>? = null,  // this is removed in swagger 17. april Requirement  changed
        var installed : Boolean,
        var pinned : Boolean?
) : Serializable