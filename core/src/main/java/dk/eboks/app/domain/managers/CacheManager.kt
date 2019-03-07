package dk.eboks.app.domain.managers

import dk.eboks.app.storage.base.ICacheStore

interface CacheManager {
    fun registerStore(store: ICacheStore)
    fun clearStores()
    fun clearStoresMemoryOnly()
}