package dk.eboks.app.domain.models.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
@Parcelize
data class Address (
        var name : String = "",
        var addressLine1 : String = "",
        var addressLine2 : String? = "",
        var city : String = "",
        var zipCode : String = "",
        var phone : String = "",
        var link : Link?
) : Parcelable