package dk.eboks.app.domain.models.message

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */

@Parcelize
data class Sign(
        var status : Status,
        var reject : Int?

) : Parcelable