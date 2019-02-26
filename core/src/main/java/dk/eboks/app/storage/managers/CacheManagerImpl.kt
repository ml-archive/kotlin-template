package dk.eboks.app.storage.managers

import android.content.Context
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.storage.base.ICacheStore
import timber.log.Timber
import java.io.File

class CacheManagerImpl(val context: Context) : CacheManager {
    private val cacheStores: MutableList<ICacheStore> = ArrayList()

    override fun registerStore(store: ICacheStore) {
        cacheStores.add(store)
    }

    override fun clearStores() {
        clearStoresMemoryOnly()
        deleteCache()
        for (store in cacheStores) {
            store.clearDisc()
        }
    }

    override fun clearStoresMemoryOnly() {
        for (store in cacheStores) {
            store.clearMemory()
        }
    }

    private fun deleteCache() {
        try {
            val dir = context.cacheDir
            if (dir != null && dir.isDirectory) {
                deleteDir(dir)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        // The directory is now empty so delete it
        Timber.e("Deleting ${dir?.path} yall")
        return dir!!.delete()
    }
}