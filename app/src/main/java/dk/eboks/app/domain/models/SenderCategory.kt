package dk.eboks.app.domain.models

import android.os.Parcelable
import dk.eboks.app.domain.models.sender.Sender
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/12/2018.
 * @author   Christian
 * @since    3/12/2018.
 */
@Parcelize
data class SenderCategory(
        var id: Long,
        var name: String = "",
        var numberOfSenders: Int = 0,
        var senders : List<Sender>? = ArrayList()
) : Parcelable