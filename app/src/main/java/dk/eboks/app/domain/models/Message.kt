package dk.eboks.app.domain.models

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class Message(
    var id : String,
    var name : String,
    var unread : Boolean,
    var received : Date,
    var sender: Sender? = null,
    var recipient: Sender? = null,
    var folder : Folder? = null,
    var content : Content? = null,
    var link : Link? = null,
    var status: Status? = null,
    var numberOfAttachments: Int = 0,
    var attachments : List<Content>? = null,
    var note : String = "",
    var messageType: MessageType? = MessageType.RECEIVED
) : Serializable