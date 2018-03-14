package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message

/**
 * Created by bison on 01/02/18.
 */
interface MessagesRepository {
    fun getMessages(cached : Boolean = false, folderId : Long) : List<Message>
    fun getMessages(cached : Boolean = false, type : FolderType) : List<Message>
    fun getMessage(folderId: Long, id : String, receipt : Boolean? = null, terms : Boolean? = null) : Message
}