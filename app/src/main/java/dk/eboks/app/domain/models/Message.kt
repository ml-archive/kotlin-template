package dk.eboks.app.domain.models

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by bison on 24-06-2017.
 */
data class Message(
    var id : Long,
    var name : String,
    var isRead : Boolean,
    var date : Date,
    var sender: Sender?,
    var status: Status?,
    var attachments : List<Attachment>? = null
) : Serializable