package dk.eboks.app.domain.models.sender

import java.io.Serializable
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Status

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
data class SenderGroup (
        var id: Long,
        var name: String = "",
        var description: Description?,
        var registered: Int? = 0, // (0: No, 1: Yes)
//        var alias : List<AliasRegistration>? = null, // TODO
//        var registrations : List<AliasRegistration>? = null, // TODO
        var numberOfRegistrations: Int? = 0, // (0: No, 1: Yes, 2: Partial)
        var status : Status,
        var unreadCount: Int = 0
) : Serializable