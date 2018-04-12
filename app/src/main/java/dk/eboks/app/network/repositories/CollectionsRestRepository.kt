package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.eboks.app.injection.modules.CollectionsStore

/**
 * Created by chnt on 01/02/18.
 * @author   chnt
 * @since    01/02/18.
 */
class CollectionsRestRepository(val collectionsStore: CollectionsStore) : CollectionsRepository {

    override fun getCollections(cached: Boolean): List<CollectionContainer> {
        val result = if (cached) collectionsStore.get(0) else collectionsStore.fetch(0)
        if (result == null) {
            throw(RuntimeException("dang!"))
        }
        return result
    }
}