package dk.eboks.app.domain.models.message

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class PaymentOption(
        var name : String,
        var description : Description?,
        var status: Int?,
        var type: String  //  "type": (string) = ["betalingsservice","dibs","reepay"]

) : Serializable