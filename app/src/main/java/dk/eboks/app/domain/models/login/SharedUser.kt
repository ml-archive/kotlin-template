package dk.eboks.app.domain.models.login

import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable
import java.util.*

data class SharedUser(
        var id: Int,
        var userId: Int,
        var name: String,
        var permission: String,
        var expiryDate: Date?,
        var status: Status?

) : Serializable