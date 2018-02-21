package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Message

/**
 * Created by bison on 01/02/18.
 */
interface MessagesRepository {
    fun getMessages(cached : Boolean = false, folderId : Long) : List<Message>
    fun getMessages(cached : Boolean = false, type : FolderType) : List<Message>
    fun getMessage(cached : Boolean = false, folderId: Long, id : String) : Message
}