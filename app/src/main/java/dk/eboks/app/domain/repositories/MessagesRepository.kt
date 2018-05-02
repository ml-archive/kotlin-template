package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender

/**
 * Created by bison on 01/02/18.
 */
interface MessagesRepository {
    fun getMessages(cached : Boolean = false, folderId : Long) : List<Message>
    fun getMessages(cached : Boolean = false, type : FolderType) : List<Message>
    fun getMessage(folderId: Long, id : String, receipt : Boolean? = null, terms : Boolean? = null) : Message
    fun getMessageReplyForm(folderId: Long, id : String) : ReplyForm
    fun submitMessageReplyForm(msg : Message, form: ReplyForm)
    fun getMessagesBySender(cached: Boolean, senderId : Long): List<Message>
    fun hasCachedMessageFolder(folder: Folder): Boolean
    fun hasCachedMessageSender(sender: Sender): Boolean
}