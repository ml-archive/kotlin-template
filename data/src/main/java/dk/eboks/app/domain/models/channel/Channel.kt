package dk.eboks.app.domain.models.channel

import android.os.Parcelable
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 16/02/2018.
 */
@Parcelize
data class Channel(
    var id: Int,
    var name: String,
    var payoff: String,
    var description: String?, // this is removed in swagger 17. april (Been readded in the latest)
    var status: Status?,
    var logo: Image?,
    var image: Image?,
    var background: ChannelColor, // this is changed in swagger 17. april  now a string
    var requirements: List<Requirement>? = null, // this is removed in swagger 17. april Requirement  changed
    var installed: Boolean,
    var pinned: Boolean?,
    var supportPinned: Boolean? = null
) : Parcelable