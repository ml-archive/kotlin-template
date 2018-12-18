package dk.eboks.app.domain.models.sender

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Description
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/19/2018.
 * @author   Christian
 * @since    3/19/2018.
 */
@Parcelize
data class CollectionContainer(
        var type: String? = "",
        var description: Description? = null,
        var segment : Segment? = null,
        var sender: Sender? = null,
    var senders : List<Sender>? = null
) : Parcelable