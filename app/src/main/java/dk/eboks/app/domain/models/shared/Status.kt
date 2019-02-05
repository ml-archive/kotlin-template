package dk.eboks.app.domain.models.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * Created by bison on 24-06-2017.
 */

@Parcelize
data class Status(
    var important: Boolean = false,
    var title: String? = null,
    var text: String? = null,
    var type: Int,
    var date: Date? = null
) : Parcelable