package dk.eboks.app.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Folder(
    var id : Long = 0,
    var name : String = "",
    var type : FolderType = FolderType.FOLDER,
    var unreadCount : Int = 0,
    var iconImageUrl : String = "",
    var folders : List<Folder> = ArrayList()
) : Serializable