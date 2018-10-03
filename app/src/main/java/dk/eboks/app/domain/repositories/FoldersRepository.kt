package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderRequest

/**
 * Created by bison on 01/02/18.
 */
interface FoldersRepository {
    fun getFolders(cached : Boolean = false) : List<Folder>
    fun createFolder(folderRequest: FolderRequest)
}