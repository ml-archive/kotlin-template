package dk.eboks.app.domain.models.folder

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Folder(
    var id: Int = 0,
    var name: String = "",
    val type: FolderType? = FolderType.FOLDER,
    val unreadCount: Int = 0,
    val folders: List<Folder> = arrayListOf(),
    var parentFolder: Folder? = null
) : Serializable