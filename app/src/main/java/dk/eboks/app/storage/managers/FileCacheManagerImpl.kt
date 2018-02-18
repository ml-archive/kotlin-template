package dk.eboks.app.storage.managers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.models.Content
import dk.eboks.app.storage.base.GsonFileStorageRepository
import timber.log.Timber
import java.io.File

/**
 * Created by bison on 17-02-2018.
 * TODO implement max size and delete old entries from disk
 */
class FileCacheManagerImpl(val context: Context, val gson: Gson) : FileCacheManager {
    var cache : MutableMap<String, CacheEntry>
    val cacheStore = GsonCacheStore()
    val cacheDir : File

    init {
        val type = object : TypeToken<HashMap<String, CacheEntry>>(){}.type
        try {
            cache = cacheStore.load(type)
            Timber.e("Loaded file cache with ${cache.size} entries")
            for(entry in cache)
            {
                Timber.e("Entry: ${entry.key} = ${entry.value.filename}")
            }

        }
        catch (t : Throwable)
        {
            Timber.e("Filecache empty")
            cache = HashMap()
        }
        cacheDir = File(context.cacheDir, "filecache")
        if(!cacheDir.exists())
        {
            Timber.e("Cache dir filecache does not exist, creating one")
            cacheDir.mkdirs()
        }
    }

    override fun findContent(id: String): Content? {
        cache[id]?.let {
            return it.content
        }
        return null
    }

    override fun getCachedContentFileName(content: Content): String? {
        cache[content.id]?.let { entry->
            return entry.filename
        }
        return null
    }

    override fun cacheContent(filename: String, content: Content)
    {
        val entry = CacheEntry(filename, content)
        cache[content.id] = entry
        cacheStore.save(cache)
        Timber.e("Added entry ${content.id} = ${entry.filename}")
    }

    override fun generateFileName(content: Content) : String
    {
        return "filecache/${content.id}"
    }

    override fun getAbsolutePath(filename: String) : String
    {
        val downloadedFile = File(context.cacheDir, filename)
        if(!downloadedFile.exists())
        {
            Timber.e("Error file $filename does not exist")
        }
        return downloadedFile.absolutePath
    }

    inner class CacheEntry(var filename: String, var content: Content)
    inner class GsonCacheStore : GsonFileStorageRepository<MutableMap<String, CacheEntry>>(context, gson, "file_cache.json")
}