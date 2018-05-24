package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import dk.eboks.app.util.guard
import timber.log.Timber

typealias SenderIdMessageStore = CacheStore<Long, List<Message>>
typealias FolderIdMessageStore = CacheStore<Int, List<Message>>
typealias CategoryMessageStore = CacheStore<String, List<Message>>

/**
 * Created by bison on 01/02/18.
 */
class MessagesRestRepository(val context: Context, val api: Api, val gson: Gson) : MessagesRepository {

    val folderIdMessageStore: FolderIdMessageStore by lazy {
        FolderIdMessageStore(context, gson, "folder_id_message_store.json", object : TypeToken<MutableMap<Long, List<Message>>>() {}.type, { key ->
            val response = api.getMessages(key).execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    val highlightsMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(context, gson, "highlights_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
            val response = api.getHighlights().execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    val latestMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(context, gson, "latest_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
            val response = api.getLatest().execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    val unreadMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(context, gson, "unread_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
            val response = api.getUnread().execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    val uploadsMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(context, gson, "uploads_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
            val response = api.getUploads().execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }


    val senderIdMessageStore: SenderIdMessageStore by lazy {
        SenderIdMessageStore(context, gson, "sender_id_message_store.json", object : TypeToken<MutableMap<Long, List<Message>>>() {}.type, { key ->
            val response = api.getMessagesBySender(key).execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    init {

    }

    override fun getMessages(cached: Boolean, folderId : Int): List<Message>
    {
        val res = if(cached) folderIdMessageStore.get(folderId) else folderIdMessageStore.fetch(folderId)
        if(res != null)
            return res
        else
            return ArrayList()
    }

    /*
    override fun getMessages(cached: Boolean, type: FolderType): List<Message> {
        val res = if(cached) folderTypeMessageStore.get(type.toString()) else folderTypeMessageStore.fetch(type.toString())
        if(res != null)
            return res
        else
            return ArrayList()
    }
    */

    override fun getHighlights(cached: Boolean): List<Message> {
        val res = if(cached) highlightsMessageStore.get("highlights") else highlightsMessageStore.fetch("highlights")
        if(res != null)
            return res
        else
            return ArrayList()
    }

    override fun getLatest(cached: Boolean): List<Message> {
        val res = if(cached) latestMessageStore.get("latest") else latestMessageStore.fetch("latest")
        if(res != null)
            return res
        else
            return ArrayList()
    }

    override fun getUnread(cached: Boolean): List<Message> {
        val res = if(cached) unreadMessageStore.get("unread") else unreadMessageStore.fetch("unread")
        if(res != null)
            return res
        else
            return ArrayList()
    }

    override fun getUploads(cached: Boolean): List<Message> {
        val res = if(cached) uploadsMessageStore.get("uploads") else unreadMessageStore.fetch("uploads")
        if(res != null)
            return res
        else
            return ArrayList()
    }


    override fun getMessagesBySender(cached: Boolean, senderId : Long): List<Message> {
        val res = if(cached) senderIdMessageStore.get(senderId) else senderIdMessageStore.fetch(senderId)
        if(res != null)
            return res
        else
            return ArrayList()
    }


    override fun getMessage(folderId: Int, id: String, receipt : Boolean?, terms : Boolean?) : Message {
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

    override fun getMessageReplyForm(folderId: Int, id: String) : ReplyForm {
        val call = api.getMessageReplyForm(id, folderId)
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

    override fun submitMessageReplyForm(msg : Message, form: ReplyForm) {
        msg.folder?.let {
            val call = api.submitMessageReplyForm(msg.id, msg.folder?.id ?: 0, form)
            val result = call.execute()
            result?.let { response ->
                if(response.isSuccessful)
                {
                    return
                }
                // attempt to parse error
                response.errorBody()?.string()?.let { error_str ->
                    Timber.e("Received error body $error_str")
                    throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
                }
            }
            throw(RuntimeException())
        }.guard {
            throw(RuntimeException("submitMessageReplyForm message.folder is null"))
        }

    }

    override fun hasCachedMessageFolder(folder: Folder): Boolean {
        when(folder.type)
        {
            FolderType.HIGHLIGHTS -> {
                return highlightsMessageStore.containsKey("highlights")
            }
            FolderType.LATEST -> {
                return latestMessageStore.containsKey("latest")
            }
            FolderType.UNREAD -> {
                return unreadMessageStore.containsKey("unread")
            }
            FolderType.UPLOADS -> {
                return uploadsMessageStore.containsKey("uploads")
            }
            else -> {
                return folderIdMessageStore.containsKey(folder.id)
            }
        }
    }

    override fun hasCachedMessageSender(sender: Sender): Boolean {
        return senderIdMessageStore.containsKey(sender.id)
    }
}