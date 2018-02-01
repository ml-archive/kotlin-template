package dk.eboks.app.network.rest

import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.eboks.app.injection.modules.FolderStore
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class FoldersRestRepository(val folderStore: FolderStore) : FoldersRepository {

    override fun getFolders(cached: Boolean): List<Folder> {
        try {
            val result = if(cached) folderStore.get(0).blockingGet() else folderStore.fetch(0).blockingGet()
            if(result == null) {
                throw(RepositoryException(-1, "darn"))
            }
            return result
        }
        catch (e : Throwable)
        {
            e.printStackTrace()
            if(e.cause != null) {
                when(e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            }
            else
                throw(RepositoryException(-1, "Unknown"))
        }
    }
}