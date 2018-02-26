package dk.eboks.app.network.repositories

import com.google.gson.Gson
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.exceptions.RepositoryException
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.ServerError
import dk.eboks.app.injection.modules.*
import dk.eboks.app.network.Api
import dk.eboks.app.util.guard
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class MessagesRestRepository(val api: Api, val gson: Gson, val listMessageStore: ListMessageStore, val folderTypeMessageStore: FolderTypeMessageStore) : MessagesRepository {

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

    override fun getMessage(folderId: Long, id: String) : Message {
        try {
            val call = api.getMessage(id, folderId)
            val result = call.execute()
            result?.let { response ->
                if(response.isSuccessful)
                {
                    return response.body() ?: throw(RepositoryException(-1, "Unknown"))
                }
                // attempt to parse error
                response.errorBody()?.string()?.let { error_str ->
                    Timber.e("Received error body $error_str")
                    throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
                }
            }
        }
        catch (e : Throwable)
        {

            when(e) {
                is ServerErrorException -> {
                    Timber.e("Server error exception (runtime)")
                    throw(e)
                }
                is UnknownHostException -> {
                    Timber.e("unknown host")
                    throw(RepositoryException(-1, "UnknownHostException"))
                }
            }
            if(BuildConfig.DEBUG) e.printStackTrace()
        }
        throw(RepositoryException())
    }
}