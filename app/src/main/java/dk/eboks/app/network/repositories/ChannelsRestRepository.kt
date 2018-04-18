package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import timber.log.Timber

typealias ChannelListStore = CacheStore<String, MutableList<Channel>>
typealias ChannelControlStore = CacheStore<Long, HomeContent>


/**
 * Created by bison on 01/02/18.
 */
class ChannelsRestRepository(val context: Context, val api: Api, val gson: Gson) : ChannelsRepository {

    val channelStore: ChannelListStore by lazy {
        ChannelListStore(context, gson, "channel_list_store.json", object : TypeToken<MutableMap<String, MutableList<Channel>>>() {}.type, { key ->
            val response = if(key == "pinned") api.getChannelsPinned().execute() else api.getChannels().execute()
            var result : MutableList<Channel>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    val channelControlStore: ChannelControlStore by lazy {
        ChannelControlStore(context, gson, "channel_control_store.json", object : TypeToken<MutableMap<Long, HomeContent>>() {}.type, { key ->
            val response = api.getChannelHomeContent(key).execute()
            var result : HomeContent? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

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

    override fun getChannelHomeContent(id: Long, cached: Boolean): HomeContent {
        val res = if(cached) channelControlStore.get(id) else channelControlStore.fetch(id)
        if(res != null)
            return res
        else
            throw(RuntimeException())
    }

    override fun hasCachedChannelList(key : String) : Boolean
    {
        return channelStore.containsKey(key)
    }

    override fun hasCachedChannelControl(key: Long): Boolean {
        return channelControlStore.containsKey(key)
    }
}