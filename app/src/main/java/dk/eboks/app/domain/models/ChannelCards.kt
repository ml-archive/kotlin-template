package dk.eboks.app.domain.models

import android.util.EventLogTags
import java.io.Serializable

/**
 * Created by thsk on 16/02/2018.
 */


data class ChannelCards(
        var id : Int,
        var name : String,
        var payoff : String,
        /*
        var description: Description?,
        var status: Status?,
        var logo : Image?,
        var image : Image?,
        var background : Color?,
        var requirements : Array<Requirement>? = null,
        */
        var installed : Boolean?,
        var pinned : Boolean?
) : Serializable