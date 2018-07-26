package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.models.message.StorageInfo
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import dk.eboks.app.util.CountingFileRequestBody
import dk.eboks.app.util.guard
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MultipartBody





typealias SenderIdMessageStore = CacheStore<Long, List<Message>>
typealias FolderIdMessageStore = CacheStore<Int, List<Message>>
typealias CategoryMessageStore = CacheStore<String, List<Message>>

/**
 * Created by bison on 01/02/18.
 */
class MessagesRestRepository(val context: Context, val api: Api, val gson: Gson, val cacheManager: CacheManager, val okHttpClient: OkHttpClient) : MessagesRepository {

    val folderIdMessageStore: FolderIdMessageStore by lazy {
        FolderIdMessageStore(cacheManager, context, gson, "folder_id_message_store.json", object : TypeToken<MutableMap<Long, List<Message>>>() {}.type, { key ->
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
        CategoryMessageStore(cacheManager, context, gson, "highlights_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
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
        CategoryMessageStore(cacheManager, context, gson, "latest_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
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
        CategoryMessageStore(cacheManager, context, gson, "unread_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
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
        CategoryMessageStore(cacheManager, context, gson, "uploads_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
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
        SenderIdMessageStore(cacheManager, context, gson, "sender_id_message_store.json", object : TypeToken<MutableMap<Long, List<Message>>>() {}.type, { key ->
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

    override fun getMessagesByFolder(folderId : Int, offset : Int, limit : Int): List<Message>
    {
        val response = api.getMessages(folderId, offset, limit).execute()
        if(response.isSuccessful) {
            response.body()?.let {
                return it
            }.guard { return ArrayList() }
        }
        return ArrayList()
    }

    override fun getMessagesBySender(senderId : Long, offset : Int, limit : Int): List<Message> {

        val response = api.getMessagesBySender(senderId, offset, limit).execute()
        if(response.isSuccessful) {
            response.body()?.let {
                return it
            }.guard { return ArrayList() }
        }
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
        val res = if(cached) uploadsMessageStore.get("uploads") else uploadsMessageStore.fetch("uploads")
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

    override fun updateMessage(message: Message, messagePatch: MessagePatch) {
        val call = if(message.folderId != 0)
            api.updateMessage(message.folderId, message.id, messagePatch)
        else if(message.folder != null) {
            api.updateMessage(message.folder!!.id, message.id, messagePatch)
        }
        else
            throw(RuntimeException("Could not resolve message folder id!!!"))

        val result = call.execute()
        result?.let { response ->
            if(response.isSuccessful)
            {
                return
            }
        }

        throw(RuntimeException())
    }

    override fun getStorageInfo(): StorageInfo {
        api.getStorageInfo().execute()?.body()?.let {
            return it
        }
        throw(RuntimeException())
    }

    override fun getLatestUploads(offset : Int?, limit : Int?): List<Message> {
        api.getUploads(offset, limit).execute()?.body()?.let {
            return it
        }
        throw(RuntimeException())
    }

    override fun uploadFileAsMessage(folderId : Int, filename : String, uriString : String, mimetype : String, callback: (Long) -> Unit)
    {
        var url = "${Config.getApiUrl()}mail/folders/$folderId/messages"

        val body = CountingFileRequestBody(FilePickerUriHelper.getFile(context, uriString), mimetype, callback)

        val formBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(filename, filename, body)
                //.addFormDataPart("filename", filename)
                .build()

        val request = Request.Builder()
                .url(url)
                .put(formBody)
                .build()
        val response = okHttpClient.newCall(request).execute()
        if(!response.isSuccessful)
            throw(RuntimeException())
    }
}