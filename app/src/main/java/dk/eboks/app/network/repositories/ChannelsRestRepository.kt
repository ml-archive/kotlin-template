package dk.eboks.app.network.repositories

import com.google.gson.Gson
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.exceptions.RepositoryException
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.injection.modules.ListChannelStore
import dk.eboks.app.network.Api
import dk.eboks.app.network.base.SynchronizedBaseRepository
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class ChannelsRestRepository(val api: Api, val gson: Gson, val channelStore: ListChannelStore) : ChannelsRepository, SynchronizedBaseRepository() {

    override fun getChannels(cached: Boolean): List<Channel> {
        try {
            lock()
            val result = if(cached) channelStore.get(0).blockingGet() else channelStore.fetch(0).blockingGet()
            unlock()
            if(result == null) {
                throw(RepositoryException(-1, "darn"))
            }
            return result
        }
        catch (e : Throwable)
        {
            e.printStackTrace()
            if(e.cause != null) {
                when(e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            }
            else
                throw(RepositoryException(-1, "Unknown"))
        }
        finally {
            unlock()
        }
    }

    override fun getChannel(id: Long) : Channel {
        try {
            val call = api.getChannel(id)
            val result = call.execute()
            result?.let { response ->
                if(response.isSuccessful)
                {
                    return response.body() ?: throw(RepositoryException(-1, "Unknown"))
                }
                // attempt to parse error
                response.errorBody()?.string()?.let { error_str ->
                    Timber.e("Received error body $error_str")
                    throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
                }
            }
        }
        catch (e : Throwable)
        {

            when(e) {
                is ServerErrorException -> {
                    Timber.e("Server error exception (runtime)")
                    throw(e)
                }
                is UnknownHostException -> {
                    Timber.e("unknown host")
                    throw(RepositoryException(-1, "UnknownHostException"))
                }
            }
            if(BuildConfig.DEBUG) e.printStackTrace()
        }
        throw(RepositoryException())
    }
}