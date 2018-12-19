package dk.eboks.app.domain.models.message

import android.os.Parcelable
import dk.eboks.app.domain.models.protocol.Metadata
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Messages(
    var items: List<Message>,
    var metadata: Metadata? = null
) : Parcelable