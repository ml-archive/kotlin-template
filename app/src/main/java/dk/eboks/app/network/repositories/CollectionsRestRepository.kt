package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore

typealias CollectionsStore = CacheStore<Int, List<CollectionContainer>>

/**
 * Created by chnt on 01/02/18.
 * @author   chnt
 * @since    01/02/18.
 */
class CollectionsRestRepository(
    val context: Context,
    val api: Api,
    val gson: Gson,
    val cacheManager: CacheManager
) : CollectionsRepository {

    private val collectionsStore: CollectionsStore by lazy {
        CollectionsStore(
            cacheManager,
            context,
            gson,
            "collectons_store.json",
            object : TypeToken<MutableMap<Int, List<CollectionContainer>>>() {}.type
        ) { key ->
            val response = api.getCollections().execute()
            var result: List<CollectionContainer>? = null
            response?.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    override fun getCollections(cached: Boolean): List<CollectionContainer> {
        return (if (cached) collectionsStore.get(0) else collectionsStore.fetch(0))
            ?: throw(RuntimeException("dang!"))
    }
}