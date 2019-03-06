package dk.eboks.app.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Image(
    var url: String?,
    var text: String? = null,
    var version: String? = null
) : Parcelable