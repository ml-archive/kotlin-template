package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.models.message.StorageInfo
import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.Link

/**
 * Created by bison on 01/02/18.
 */
interface MessagesRepository {

    fun deleteMessage(folderId: Int, messageId: String)

    fun getMessagesByFolder(folderId: Int, offset: Int = 0, limit: Int = 20): List<Message>
    fun getMessagesBySender(senderId: Long, offset: Int = 0, limit: Int = 20): List<Message>
    // fun getMessages(cached : Boolean = false, type : FolderType) : List<Message>
    fun getMessage(
        folderId: Int,
        id: String,
        receipt: Boolean? = null,
        acceptedPrivateTerms: Boolean? = null
    ): Message

    fun getMessageReplyForm(folderId: Int, id: String): ReplyForm
    fun submitMessageReplyForm(msg: Message, form: ReplyForm)
    fun hasCachedMessageFolder(folder: Folder): Boolean
    fun hasCachedMessageSender(sender: Sender): Boolean
    fun getHighlights(cached: Boolean): List<Message>
    fun getLatest(cached: Boolean): List<Message>
    fun getUnread(cached: Boolean): List<Message>
    fun getUploads(cached: Boolean): List<Message>
    fun updateMessage(message: Message, messagePatch: MessagePatch)
    fun getStorageInfo(): StorageInfo
    fun getLatestUploads(offset: Int? = null, limit: Int? = null): List<Message>

    fun getPaymentDetails(folderId: Int, messageId: String): Payment

    fun getPaymentLink(folderId: Int, messageId: String, type: String): Link?

    fun uploadFileAsMessage(
        folderId: Int,
        filename: String,
        uriString: String,
        mimetype: String,
        callback: (Double) -> Unit
    )
}