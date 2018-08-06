package dk.eboks.app.domain.models.message

import com.google.gson.annotations.SerializedName
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class Message(
    var id : String,
    var subject : String,
    var received : Date,
    var unread : Boolean,
    var sender: Sender? = null,
    var type: MessageType? = MessageType.RECEIVED,
    var recipient: Sender? = null,
    var folder : Folder? = null,
    var folderId : Int = 0,
    var content : Content? = null,
    var attachments : List<Content>? = null,
    var numberOfAttachments: Int = 0,
    var payment: Payment?,
    var sign: Sign?,
    var reply: Status?,
    var link : Link? = null,
    var paymentStatus: Status? = null,
    var signStatus: Status? = null,
    var replyStatus: Status? = null,
    @SerializedName("locked")
    var lockStatus: Status? = null,
    var status: Status? = null,
    @SerializedName("Note")
    var note : String = ""
) : Serializable