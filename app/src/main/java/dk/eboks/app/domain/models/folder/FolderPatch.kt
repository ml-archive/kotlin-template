package dk.eboks.app.domain.models.folder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FolderPatch(
        var userId : Int? = null,
        var parentFolderId : Int? = null,
        var name : String? = null
) : Parcelable