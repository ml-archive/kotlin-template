package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.Sender
import dk.eboks.app.domain.repositories.RepositoryException
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.injection.modules.SenderStore
import dk.eboks.app.network.base.SynchronizedBaseRepository
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class SendersRestRepository(val senderStore: SenderStore) : SendersRepository, SynchronizedBaseRepository() {

    override fun getSenders(cached: Boolean): List<Sender> {
        try {
            lock()
            val result = if(cached) senderStore.get(0).blockingGet() else senderStore.fetch(0).blockingGet()
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