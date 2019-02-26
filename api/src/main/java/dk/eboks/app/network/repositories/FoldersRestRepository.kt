package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import javax.inject.Inject

typealias FolderListStore = CacheStore<Int, List<Folder>>

/**
 * Created by bison on 01/02/18.
 */
class FoldersRestRepository @Inject constructor(
    private val context: Context,
    private val api: Api,
    private val gson: Gson,
    private val cacheManager: CacheManager
) : FoldersRepository {

    private var userId: Int? = null

    private val folderStore: FolderListStore by lazy {
        FolderListStore(
                cacheManager,
                context,
                gson,
                "folder_list_store.json",
                object : TypeToken<MutableMap<Int, List<Folder>>>() {}.type
        ) { key ->
            val response = api.getFolders(userId).execute()
            var result: List<Folder>? = null
            response.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    override fun getFolders(cached: Boolean, userId: Int?): List<Folder> {
        this.userId = userId
        return (if (cached) folderStore.get(0) else folderStore.fetch(0)) ?: ArrayList()
    }

    override fun createFolder(folderRequest: FolderRequest) {
        val response = api.createFolder(folderRequest).execute()
        response.let {
            if (it.isSuccessful) {
                return
            }
        }
    }

    override fun editFolder(folderId: Int, folderRequest: FolderRequest) {
        val response = api.editFolder(folderId, folderRequest).execute()
        response.let {
            if (it.isSuccessful) {
                return
            }
        }
    }

    override fun deleteFolder(folderId: Int) {
        val response = api.deleteFolder(folderId).execute()
        response.let {
            if (it.isSuccessful) {
                return
            }
        }
    }
}