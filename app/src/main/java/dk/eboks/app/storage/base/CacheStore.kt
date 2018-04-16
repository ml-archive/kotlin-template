package dk.eboks.app.storage.base

import android.content.Context
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.models.message.Message
import timber.log.Timber

class CacheStore<K,V>(val context: Context, val gson: Gson, val filename : String, mapType: Type, val fetchFunction : (K)->V?) {

    private var cacheMap : MutableMap<K, V>
    private val store = GsonCacheStore()
    //val mapType  = object : TypeToken<MutableMap<K, V>>() {}.type

    init {
        try {
            cacheMap = store.load(mapType)
            Timber.e("Cache map: $cacheMap")
        }
        catch (t : Throwable)
        {
            cacheMap = HashMap()
        }
        Timber.d("Initialized object cache ${store.filename} with ${cacheMap.size} entries")
    }

    /*
        Always attempts to use the fetch function to retrieve the data
     */
    fun fetch(key : K) : V?
    {
        val res = fetchFunction(key)
        res?.let { put(key, res) }
        return res
    }

    /*
        Looks for the data in the cache first, if not found uses the fetch function
     */
    fun get(key : K) : V?
    {
        if(!cacheMap.containsKey(key))
        {
            val res = fetchFunction(key)
            res?.let { put(key, res) }
            return res
        }
        else
            return cacheMap[key]
    }

    fun put(key : K, value : V)
    {
        cacheMap[key] = value
        store.save(cacheMap)
    }

    fun isEmpty() : Boolean
    {
        return cacheMap.isEmpty()
    }

    fun containsKey(key : K) : Boolean
    {
        return cacheMap.containsKey(key)
    }

    inner class GsonCacheStore : GsonFileStorageRepository<MutableMap<K, V>>(context, gson, filename)
}