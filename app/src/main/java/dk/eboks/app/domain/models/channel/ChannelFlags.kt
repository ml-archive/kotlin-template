package dk.eboks.app.domain.models.channel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 19/02/2018.
 */
@Parcelize
data class ChannelFlags(
    var pinned: Boolean = false
) : Parcelable