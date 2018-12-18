package dk.eboks.app.domain.models.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Link(
    var text : String? = null,
    var url : String = ""
) : Parcelable