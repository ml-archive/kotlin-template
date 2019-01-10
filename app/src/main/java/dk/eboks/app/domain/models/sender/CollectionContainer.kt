package dk.eboks.app.domain.models.sender

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/19/2018.
 * @author   Christian
 * @since    3/19/2018.
 */
@Parcelize
data class CollectionContainer(
    val description: String?,
    val segment: Segment?,
    val sender: Sender?,
    val senders: List<Sender>?,
    val type: String?
) : Parcelable