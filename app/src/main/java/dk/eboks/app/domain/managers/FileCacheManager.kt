package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.message.Content

/**
 * Created by bison on 17-02-2018.
 */
interface FileCacheManager {
    fun findContent(id: String): Content?
    fun cacheContent(filename: String, content: Content)
    fun generateFileName(content: Content): String
    fun getCachedContentFileName(content: Content): String?
    fun getAbsolutePath(filename: String): String
    fun copyContentToExternalStorage(content: Content): String?
    fun isExternalStorageWritable(): Boolean
    fun isExternalStorageReadable(): Boolean
    fun clearMemoryOnly()
}