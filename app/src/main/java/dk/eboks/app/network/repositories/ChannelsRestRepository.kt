package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.eboks.app.injection.modules.ListChannelStore
import dk.eboks.app.network.base.SynchronizedBaseRepository
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class ChannelsRestRepository(val channelStore: ListChannelStore) : ChannelsRepository, SynchronizedBaseRepository() {

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
}