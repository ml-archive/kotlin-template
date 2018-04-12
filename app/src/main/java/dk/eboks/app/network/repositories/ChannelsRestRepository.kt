package dk.eboks.app.network.repositories

import com.google.gson.Gson
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.injection.modules.ChannelListStore
import dk.eboks.app.network.Api
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class ChannelsRestRepository(val api: Api, val gson: Gson, val channelStore: ChannelListStore) : ChannelsRepository {

    override fun getChannels(cached: Boolean): List<Channel> {

        val res = if(cached) channelStore.get("main") else channelStore.fetch("main")
        if(res != null)
            return res
        else
            return ArrayList()
    }

    override fun getPinnedChannels(cached: Boolean): MutableList<Channel> {
        val res = if(cached) channelStore.get("pinned") else channelStore.fetch("pinned")
        if(res != null)
            return res
        else
            return ArrayList()

    }

    override fun getChannel(id: Long) : Channel {
        val call = api.getChannel(id)
        val result = call.execute()
        result?.let { response ->
            if(response.isSuccessful)
            {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
            // attempt to parse error
            response.errorBody()?.string()?.let { error_str ->
                Timber.e("Received error body $error_str")
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw(RuntimeException())
    }

    override fun getChannelHomeContent(id: Long): HomeContent {
        val call = api.getChannelHomeContent(id)
        val result = call.execute()
        result?.let { response ->
            if(response.isSuccessful)
            {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
            // attempt to parse error
            response.errorBody()?.string()?.let { error_str ->
                Timber.e("Received error body $error_str")
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw(RuntimeException())
    }

}