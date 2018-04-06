package dk.eboks.app.network.repositories

import com.google.gson.Gson
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.injection.modules.ListChannelStore
import dk.eboks.app.network.Api
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class ChannelsRestRepository(val api: Api, val gson: Gson, val channelStore: ListChannelStore) : ChannelsRepository {

    override fun getChannels(cached: Boolean): List<Channel> {
        val result = if(cached) channelStore.get(0).blockingGet() else channelStore.fetch(0).blockingGet()
        if(result == null) {
            throw(RuntimeException("darn"))
        }
        return result
    }

    override fun getPinnedChannels(): MutableList<Channel> {
        val call = api.getChannelsPinned()
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