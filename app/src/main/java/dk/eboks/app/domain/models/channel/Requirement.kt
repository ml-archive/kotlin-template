package dk.eboks.app.domain.models.channel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 19/02/2018.
 */
@Parcelize
data class Requirement(
        var name : String,
        var value : String?,
        var type : RequirementType?,
        var verified : Boolean? = false
) : Parcelable