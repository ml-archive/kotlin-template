package dk.eboks.app.domain.models.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class MessagePatch(
    var unread: Boolean? = null,
    var archive: Boolean? = null,
    var folderId: Int? = null,
    var note: String? = null
) : Parcelable {

    fun isApplicableForUppload(): Boolean = unread == null && archive == null
}