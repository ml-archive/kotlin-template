package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.models.sender.Sender

/**
 * Created by bison on 01/02/18.
 */
interface MessagesRepository {
    fun getMessages(cached : Boolean = false, folderId : Int) : List<Message>
    //fun getMessages(cached : Boolean = false, type : FolderType) : List<Message>
    fun getMessage(folderId: Int, id : String, receipt : Boolean? = null, terms : Boolean? = null) : Message
    fun getMessageReplyForm(folderId: Int, id : String) : ReplyForm
    fun submitMessageReplyForm(msg : Message, form: ReplyForm)
    fun getMessagesBySender(cached: Boolean, senderId : Long): List<Message>
    fun hasCachedMessageFolder(folder: Folder): Boolean
    fun hasCachedMessageSender(sender: Sender): Boolean
    fun getHighlights(cached: Boolean): List<Message>
    fun getLatest(cached: Boolean): List<Message>
    fun getUnread(cached: Boolean): List<Message>
    fun getUploads(cached: Boolean): List<Message>
    fun updateMessage(message: Message, messagePatch: MessagePatch)
}