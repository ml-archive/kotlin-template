package dk.eboks.app.storage.base

import android.content.Context
import com.google.gson.Gson
import dk.eboks.app.App
import dk.eboks.app.domain.managers.CacheManager
import timber.log.Timber
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

class CacheStore<K, V>(
    val cacheManager: CacheManager,
    val context: Context,
    val gson: Gson,
    val filename: String,
    mapType: Type,
    val fetchFunction: (K) -> V?
) : ICacheStore {
    private var cacheMap: MutableMap<K, V>
    private val store = GsonCacheStore()

    // val mapType  = object : TypeToken<MutableMap<K, V>>() {}.type

    init {
        // register this bad bwoi with the cachemanager
        App.instance().appComponent.inject(this as ICacheStore)
        cacheManager.registerStore(this)

        try {
            cacheMap = store.load(mapType)
            // Timber.e("Cache map: $cacheMap")
        } catch (t: Throwable) {
            cacheMap = ConcurrentHashMap()
        }
        Timber.d("Initialized object cache ${store.filename} with ${cacheMap.size} entries")
    }

    /*
        Always attempts to use the fetch function to retrieve the data
     */
    fun fetch(key: K): V? {
        val res = fetchFunction(key)
        res?.let { put(key, res) }
        return res
    }

    /*
        Looks for the data in the cache first, if not found uses the fetch function
     */
    fun get(key: K): V? {
        if (!cacheMap.containsKey(key)) {
            val res = fetchFunction(key)
            res?.let { put(key, res) }
            return res
        } else {
            Timber.v("Cache $filename found key $key")
            return cacheMap[key]
        }
    }

    fun put(key: K, value: V) {
        cacheMap[key] = value
        store.save(cacheMap)
    }

    fun isEmpty(): Boolean {
        return cacheMap.isEmpty()
    }

    fun containsKey(key: K): Boolean {
        return cacheMap.containsKey(key)
    }

    override fun clearMemory() {
        cacheMap.clear()
    }

    override fun clearDisc() {
        Timber.e("Deleting cache file $filename")
        context.deleteFile(filename)
    }

    inner class GsonCacheStore :
        GsonFileStorageRepository<MutableMap<K, V>>(context, gson, filename)
}