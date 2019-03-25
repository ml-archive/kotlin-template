package dk.eboks.app.domain.models.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Description(
    var text: String,
    var title: String? = null,
    var format: DescriptionFormat? = DescriptionFormat.TEXT,
    var link: Link? = null
) : Parcelable
