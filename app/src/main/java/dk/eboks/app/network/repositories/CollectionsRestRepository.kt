package dk.eboks.app.network.repositories

import dk.eboks.app.domain.exceptions.RepositoryException
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.eboks.app.injection.modules.CollectionsStore
import dk.eboks.app.network.base.SynchronizedBaseRepository
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by chnt on 01/02/18.
 * @author   chnt
 * @since    01/02/18.
 */
class CollectionsRestRepository(val collectionsStore: CollectionsStore) : CollectionsRepository, SynchronizedBaseRepository() {

    override fun getCollections(cached: Boolean): List<CollectionContainer> {
        try {
            lock()
            val result = if (cached) collectionsStore.get(0).blockingGet() else collectionsStore.fetch(0).blockingGet()
            unlock()
            if (result == null) {
                throw(RepositoryException(-1, "dang!"))
            }
            return result
        } catch (e: Throwable) {
            e.printStackTrace()
            if (e.cause != null) {
                when (e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            } else
                throw(RepositoryException(-1, "Unknown"))
        } finally {
            unlock()
        }
    }
}