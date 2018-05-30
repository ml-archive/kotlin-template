package dk.eboks.app.domain.models.channel

import java.io.Serializable

/**
 * Created by thsk on 19/02/2018.
 */
data class ChannelState(
        var selectedChannel: Channel? = null
) : Serializable