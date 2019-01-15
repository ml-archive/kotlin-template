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

enum class CollectionContainerTypeEnum(val value: String?) {
    SEGMENT("segment"),
    SENDER("sender"),
    SENDERS("senders"),
    UNKNOWN("")
}

val CollectionContainer.typeEnum: CollectionContainerTypeEnum
    get() = when (type) {
        CollectionContainerTypeEnum.SEGMENT.value -> CollectionContainerTypeEnum.SEGMENT
        CollectionContainerTypeEnum.SENDER.value -> CollectionContainerTypeEnum.SENDER
        CollectionContainerTypeEnum.SENDERS.value -> CollectionContainerTypeEnum.SENDERS
        else -> CollectionContainerTypeEnum.UNKNOWN
    }
