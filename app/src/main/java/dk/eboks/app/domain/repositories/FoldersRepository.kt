package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderPatch

/**
 * Created by bison on 01/02/18.
 */
interface FoldersRepository {
    fun getFolders(cached : Boolean = false) : List<Folder>
    fun updateFolder(folderId: Int, folderPatch: FolderPatch)
}