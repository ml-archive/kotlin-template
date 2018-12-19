package dk.eboks.app.domain.models.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 28/03/2018.
 */

@Parcelize
data class BooleanReply (
        var exists: Boolean = false
) : Parcelable