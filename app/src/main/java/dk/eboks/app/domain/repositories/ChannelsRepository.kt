package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.channel.Channel

/**
 * Created by bison on 01/02/18.
 */
interface ChannelsRepository {
    fun getChannels(cached : Boolean = false) : List<Channel>
    fun getChannel(id: Long) : Channel
}