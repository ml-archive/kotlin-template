package dk.eboks.app.storage.managers

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.storage.base.GsonFileStorageRepository
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

/**
 * Created by bison on 17-02-2018.
 * TODO implement max size and delete old entries from disk
 */
class FileCacheManagerImpl(val context: Context, val gson: Gson) : FileCacheManager {
    var cache : MutableMap<String, CacheEntry>
    val cacheStore = GsonCacheStore()
    var cacheDir : File

    init {
        val type = object : TypeToken<HashMap<String, CacheEntry>>(){}.type
        try {
            cache = cacheStore.load(type)
            Timber.e("Loaded filecache with ${cache.size} entries")
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
        createCacheDirIfNotExists()
    }

    override fun clearMemoryOnly() {
        cache = HashMap()
        cacheDir = File(context.cacheDir, "filecache")
        createCacheDirIfNotExists()
    }

    private fun createCacheDirIfNotExists()
    {
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
        createCacheDirIfNotExists() // if for some reason our cache has been deleted be sure to recreate subdir
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

    override fun copyContentToExternalStorage(content: Content) : String?
    {
        var filename = getCachedContentFileName(content)
        filename?.let {
            val ext_filename = content.title.replace("[^a-zA-Z0-9\\.\\-]", "_")
            Timber.e("Generated safe filename $ext_filename")
            val srcfile = File(getAbsolutePath(filename))
            if(!srcfile.exists()) {
                Timber.e("Cache src file ${srcfile.absolutePath} does not exist")
                return null
            }
            val destfile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ext_filename)
            Timber.e("Copying cached file to ${destfile.absolutePath}")
            try {
                copyFileToExternalStorage(srcfile, destfile)
                mediaScanFile(destfile.absolutePath, content.mimeType ?: "*/*")
                return destfile.name
            }
            catch(t : Throwable)
            {
                t.printStackTrace()
                return null
            }
        }
        return null
    }

    private fun mediaScanFile(path : String, mimetype : String)
    {
        try {
            MediaScannerConnection.scanFile(context, arrayOf(path), arrayOf(mimetype), { path, uri ->
                Timber.e("Scan completed")
            })
        }
        catch (t : Throwable)
        {
            t.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun copyFileToExternalStorage(sourceFile: File, destFile: File) {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs()

        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null

        try {
            source = FileInputStream(sourceFile).getChannel()
            destination = FileOutputStream(destFile).getChannel()
            destination!!.transferFrom(source, 0, source!!.size())
        } finally {
            if (source != null) {
                source!!.close()
            }
            if (destination != null) {
                destination!!.close()
            }
        }
    }

    /* Checks if external storage is available for read and write */
    override fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    override fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    inner class CacheEntry(var filename: String, var content: Content)
    inner class GsonCacheStore : GsonFileStorageRepository<MutableMap<String, CacheEntry>>(context, gson, "file_cache.json")
}