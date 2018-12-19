package dk.eboks.app.domain.models.sender

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Address
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 * This model covers both the API - spec'ed models of SenderDetails and SenderItem
 */

@Parcelize
data class Sender(
    var id: Long,
    var name: String = "",
    var logo: Image? = null,
    var description: Description? = null,
    var address: Address? = null,
    val type: String? = null,
    var authority: Int = 0,
    var groups: List<SenderGroup>? = null,
    var registered: Int? = 0, // (0: No, 1: Yes, 2: Partial)
    //var messages: Messages? = null,
    @SerializedName("unreadCount")
    var unreadMessageCount: Int = 0,
    var status: Status? = null
    //var unreadMessageCount: Int = 0 // todo this should be removed - its not in the draft
) : Parcelable