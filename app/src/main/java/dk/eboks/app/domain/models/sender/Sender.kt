package dk.eboks.app.domain.models.sender

import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.message.Messages
import dk.eboks.app.domain.models.shared.Address
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
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

        var messages: Messages? = null,
        var status: Status? = null,
        var unreadMessageCount: Int = 0 // todo this should be removed - its not in the draft
) : Serializable