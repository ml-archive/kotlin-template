package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.injection.modules.FolderListStore

/**
 * Created by bison on 01/02/18.
 */
class FoldersRestRepository(val folderStore: FolderListStore) : FoldersRepository {

    override fun getFolders(cached: Boolean): List<Folder> {
        val res = if(cached) folderStore.get(0) else folderStore.fetch(0)
        if(res != null)
            return res
        else
            return ArrayList()
    }
}