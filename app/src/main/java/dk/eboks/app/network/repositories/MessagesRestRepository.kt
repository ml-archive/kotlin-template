package dk.eboks.app.network.repositories

import com.google.gson.Gson
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.injection.modules.FolderTypeMessageStore
import dk.eboks.app.injection.modules.ListMessageStore
import dk.eboks.app.injection.modules.ListMessageStoreKey
import dk.eboks.app.network.Api
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class MessagesRestRepository(val api: Api, val gson: Gson, val listMessageStore: ListMessageStore, val folderTypeMessageStore: FolderTypeMessageStore) : MessagesRepository {

    override fun getMessages(cached: Boolean, folderId : Long): List<Message> {

            val result = if(cached) listMessageStore.get(ListMessageStoreKey(folderId)).blockingGet() else listMessageStore.fetch(ListMessageStoreKey(folderId)).blockingGet()
            if(result == null) {
                throw(RuntimeException("darn"))
            }
            return result
    }

    override fun getMessages(cached: Boolean, type: FolderType): List<Message> {
        val result = if(cached) folderTypeMessageStore.get(type).blockingGet() else folderTypeMessageStore.fetch(type).blockingGet()
        if(result == null) {
            throw(RuntimeException("darn"))
        }
        return result

    }

    override fun getMessage(folderId: Long, id: String, receipt : Boolean?, terms : Boolean?) : Message {

        val call = api.getMessage(id, folderId, receipt, terms)
        val result = call.execute()
        result?.let { response ->
            if(response.isSuccessful)
            {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
            // attempt to parse error
            response.errorBody()?.string()?.let { error_str ->
                Timber.e("Received error body $error_str")
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }

        throw(RuntimeException())
    }
}