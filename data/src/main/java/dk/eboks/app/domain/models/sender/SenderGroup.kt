package dk.eboks.app.domain.models.sender

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/15/2018.
 * @author Christian
 * @since 3/15/2018.
 */
@Parcelize
data class SenderGroup(
    var id: Long,
    var name: String = "",
    var description: Description?,
    var registered: Boolean? = false,
    var alias: List<Alias>? = null,
//        var registrations : List<AliasRegistration>? = null, // TODO
    var numberOfRegistrations: Int? = 0, // (0: No, 1: Yes, 2: Partial)
    var status: Status?,
    var unreadCount: Int = 0
) : Parcelable