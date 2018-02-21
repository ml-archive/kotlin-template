package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.eboks.app.injection.modules.*
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class MessagesRestRepository(val listMessageStore: ListMessageStore, val folderTypeMessageStore: FolderTypeMessageStore, val messageStore: MessageStore) : MessagesRepository {

    override fun getMessages(cached: Boolean, folderId : Long): List<Message> {
        try {
            val result = if(cached) listMessageStore.get(ListMessageStoreKey(folderId)).blockingGet() else listMessageStore.fetch(ListMessageStoreKey(folderId)).blockingGet()
            if(result == null) {
                throw(RepositoryException(-1, "darn"))
            }
            return result
        }
        catch (e : Throwable)
        {
            e.printStackTrace()
            if(e.cause != null) {
                when(e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            }
            else
                throw(RepositoryException(-1, "Unknown"))
        }
    }

    override fun getMessages(cached: Boolean, type: FolderType): List<Message> {
        try {
            val result = if(cached) folderTypeMessageStore.get(type).blockingGet() else folderTypeMessageStore.fetch(type).blockingGet()
            if(result == null) {
                throw(RepositoryException(-1, "darn"))
            }
            return result
        }
        catch (e : Throwable)
        {
            e.printStackTrace()
            if(e.cause != null) {
                when(e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            }
            else
                throw(RepositoryException(-1, "Unknown"))
        }
    }

    override fun getMessage(cached: Boolean, folderId: Long, id: String) : Message {
        try {
            val key = MessageStoreKey(folderId, id)
            val result = if(cached) messageStore.get(key).blockingGet() else messageStore.fetch(key).blockingGet()
            if(result == null) {
                throw(RepositoryException(-1, "darn"))
            }
            return result
        }
        catch (e : Throwable)
        {
            e.printStackTrace()
            if(e.cause != null) {
                when(e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            }
            else
                throw(RepositoryException(-1, "Unknown"))
        }
    }
}