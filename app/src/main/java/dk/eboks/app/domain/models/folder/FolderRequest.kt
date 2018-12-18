package dk.eboks.app.domain.models.folder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FolderRequest(
        var userId : Int?,
        var parentFolderId : Int?,
        var name : String?
) : Parcelable