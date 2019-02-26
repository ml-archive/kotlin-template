package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import javax.inject.Inject

typealias ChannelListStore = CacheStore<String, MutableList<Channel>>
typealias ChannelControlStore = CacheStore<Long, HomeContent>

/**
 * Created by bison on 01/02/18.
 */
class ChannelsRestRepository @Inject constructor(
    private val context: Context,
    private val api: Api,
    private val gson: Gson,
    private val cacheManager: CacheManager
) : ChannelsRepository {

    private val channelStore: ChannelListStore by lazy {
        dk.eboks.app.network.repositories.ChannelListStore(
            cacheManager,
            context,
            gson,
            "channel_list_store.json",
            object : TypeToken<MutableMap<String, MutableList<Channel>>>() {}.type
        ) { key ->
            val response =
                if (key == "pinned") api.getChannelsPinned().execute() else api.getChannels().execute()
            var result: MutableList<Channel>? = null
            response.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    private val channelControlStore: ChannelControlStore by lazy {
        dk.eboks.app.network.repositories.ChannelControlStore(
            cacheManager,
            context,
            gson,
            "channel_control_store.json",
            object : TypeToken<MutableMap<Long, HomeContent>>() {}.type
        ) { key ->
            val response = api.getChannelHomeContent(key).execute()
            var result: HomeContent? = null
            response.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    // TODO reenable caching here, this is a bit tricky because the cache should optimally be invalidated
    // per record for each channel when a change happen in a sub view. This brute forces a reload pending
    // a better solution, since the app will only ship with two channels anyway
    override fun getChannels(cached: Boolean): List<Channel> {
        /*
        val res = if(cached) channelStore.get("main") else channelStore.fetch("main")
        if(res != null)
            return res
        else
            return ArrayList()
            */
        val response = api.getChannels().execute()
        return response.body() ?: listOf()
    }

    override fun getInstalledChannels(): List<Channel> {
        val response = api.getChannelsInstalled().execute()
        if (response.isSuccessful)
            response.body()?.let { return it }
        return listOf()
    }

    override fun getPinnedChannels(cached: Boolean): List<Channel> {
        return (if (cached) channelStore.get("pinned") else channelStore.fetch("pinned"))
            ?: listOf()
    }

    override fun getChannel(id: Int): Channel {
        val call = api.getChannel(id)
        val result = call.execute()
        result.let { response ->
            if (response.isSuccessful) {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
        }
        throw(RuntimeException())
    }

    override fun getChannelHomeContent(id: Long, cached: Boolean): HomeContent {
        val result = api.getChannelHomeContent(id).execute()
        result.let { response ->
            if (response.isSuccessful) {
                return response.body() ?: throw(RuntimeException("Unknown"))
            } else {
                if (response.code() == 404)
                    throw(NoSuchElementException())
            }
        }
        throw(RuntimeException())
    }

    override fun hasCachedChannelList(key: String): Boolean {
        return channelStore.containsKey(key)
    }

    override fun hasCachedChannelControl(key: Long): Boolean {
        return channelControlStore.containsKey(key)
    }
}