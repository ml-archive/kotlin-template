package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.injection.modules.FolderStore
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class FoldersRestRepository(val folderStore: FolderStore) : FoldersRepository {

    override fun getFolders(cached: Boolean): List<Folder> {
        val result = if(cached) folderStore.get(0).blockingGet() else folderStore.fetch(0).blockingGet()

        if(result == null) {
            throw(RuntimeException("darn"))
        }
        return result
    }
}