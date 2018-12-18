package dk.eboks.app.domain.models.protocol

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Metadata(
    var total: Int = 0,
    var unreadCount: Int = 0
) : Parcelable