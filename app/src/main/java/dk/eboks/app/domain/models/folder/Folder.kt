package dk.eboks.app.domain.models.folder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Folder(
    var id : Int = 0,
    var name : String = "",
    var type : FolderType? = FolderType.FOLDER,
    var unreadCount : Int = 0,
    var folders : List<Folder> = ArrayList(),
    var parentFolder: Folder? = null
) : Parcelable