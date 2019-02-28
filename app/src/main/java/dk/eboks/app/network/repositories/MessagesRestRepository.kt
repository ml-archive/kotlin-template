package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.message.*
import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import dk.eboks.app.util.CountingFileRequestBody
import dk.eboks.app.util.guard
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

typealias SenderIdMessageStore = CacheStore<Long, List<Message>>
typealias FolderIdMessageStore = CacheStore<Int, List<Message>>
typealias CategoryMessageStore = CacheStore<String, List<Message>>

/**
 * Created by bison on 01/02/18.
 */
class MessagesRestRepository(
    private val context: Context,
    private val api: Api,
    private val gson: Gson,
    private val cacheManager: CacheManager,
    private val okHttpClient: OkHttpClient,
    private val appState: AppStateManager
) : MessagesRepository {

    // delete message
    override fun deleteMessage(folderId: Int, messageId: String) {
        val response = api.deleteMessage(folderId, messageId).execute()
        response?.let {
            if (it.isSuccessful) {
                return
            }
        }
    }

    private val folderIdMessageStore: FolderIdMessageStore by lazy {
        FolderIdMessageStore(
            cacheManager,
            context,
            gson,
            "folder_id_message_store.json",
            object : TypeToken<MutableMap<Long, List<Message>>>() {}.type
        ) { key ->
            val response = api.getMessages(key, appState.state?.impersoniateUser?.userId).execute()
            var result: List<Message>? = null
            response?.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    private val highlightsMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(
            cacheManager,
            context,
            gson,
            "highlights_message_store.json",
            object : TypeToken<MutableMap<String, List<Message>>>() {}.type
        ) { key ->
            val response =
                api.getHighlights(terms = appState.state?.openingState?.acceptPrivateTerms)
                    .execute()
            var result: List<Message>? = null
            response?.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    private val latestMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(
            cacheManager,
            context,
            gson,
            "latest_message_store.json",
            object : TypeToken<MutableMap<String, List<Message>>>() {}.type
        ) { key ->
            val response =
                api.getLatest(terms = appState.state?.openingState?.acceptPrivateTerms).execute()
            var result: List<Message>? = null
            response?.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    private val unreadMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(
            cacheManager,
            context,
            gson,
            "unread_message_store.json",
            object : TypeToken<MutableMap<String, List<Message>>>() {}.type
        ) { key ->
            val response =
                api.getUnread(terms = appState.state?.openingState?.acceptPrivateTerms).execute()
            var result: List<Message>? = null
            response?.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    private val uploadsMessageStore: CategoryMessageStore by lazy {
        CategoryMessageStore(
            cacheManager,
            context,
            gson,
            "uploads_message_store.json",
            object : TypeToken<MutableMap<String, List<Message>>>() {}.type
        ) { key ->
            val response = api.getUploads().execute()
            var result: List<Message>? = null
            response.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    private val senderIdMessageStore: SenderIdMessageStore by lazy {
        SenderIdMessageStore(
            cacheManager,
            context,
            gson,
            "sender_id_message_store.json",
            object : TypeToken<MutableMap<Long, List<Message>>>() {}.type
        ) { key ->
            val response =
                api.getMessagesBySender(key, appState.state?.impersoniateUser?.userId).execute()
            var result: List<Message>? = null
            response?.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    override fun getMessagesByFolder(folderId: Int, offset: Int, limit: Int): List<Message> {
        val response = api.getMessages(
            folderId,
            appState.state?.impersoniateUser?.userId,
            offset,
            limit,
            terms = appState.state?.openingState?.acceptPrivateTerms
        ).execute()
        return response.body() ?: listOf()
    }

    override fun getMessagesBySender(senderId: Long, offset: Int, limit: Int): List<Message> {
        val response = api.getMessagesBySender(
            senderId,
            appState.state?.impersoniateUser?.userId,
            offset,
            limit,
            terms = appState.state?.openingState?.acceptPrivateTerms
        ).execute()
        return response.body() ?: listOf()
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
        return (
            if (cached) highlightsMessageStore.get("highlights")
            else highlightsMessageStore.fetch("highlights")
            ) ?: listOf()
    }

    override fun getLatest(cached: Boolean): List<Message> {
        return (if (cached) latestMessageStore.get("latest") else latestMessageStore.fetch("latest"))
            ?: listOf()
    }

    override fun getUnread(cached: Boolean): List<Message> {
        return (if (cached) unreadMessageStore.get("unread") else unreadMessageStore.fetch("unread"))
            ?: listOf()
    }

    override fun getUploads(cached: Boolean): List<Message> {
        return (if (cached) uploadsMessageStore.get("uploads") else uploadsMessageStore.fetch("uploads"))
            ?: listOf()
    }

    override fun getMessage(
        folderId: Int,
        id: String,
        receipt: Boolean?,
        acceptedPrivateTerms: Boolean?
    ): Message {
        val call = api.getMessage(
            id,
            folderId,
            appState.state?.impersoniateUser?.userId,
            receipt,
            terms = acceptedPrivateTerms
        )
        val result = call.execute()
        return result.body() ?: throw(RuntimeException("Unknown"))
    }

    override fun getMessageReplyForm(folderId: Int, id: String): ReplyForm {
        return api.getMessageReplyForm(id, folderId).execute().body()
            ?: throw(RuntimeException("Unknown"))
    }

    override fun submitMessageReplyForm(msg: Message, form: ReplyForm) {
        msg.folder?.let {
            val call = api.submitMessageReplyForm(msg.id, msg.folder?.id ?: 0, form)
            val result = call.execute()
            result.let { response ->
                if (response.isSuccessful) {
                    return
                }
            }
            throw(RuntimeException())
        }.guard {
            throw(RuntimeException("submitMessageReplyForm message.folder is null"))
        }
    }

    override fun hasCachedMessageFolder(folder: Folder): Boolean {
        when (folder.type) {
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
        Timber.d("AppStateUser: ${appState.state?.impersoniateUser?.userId}")

        // Skip Archiving and Mark as Read/Unread for upploads
        if (message.type == MessageType.UPLOAD && !messagePatch.isApplicableForUppload()) {
            Timber.d("Request Skipped: ${message.type}, $messagePatch")
            return
        }

        val call = api.updateMessage(
            message.findFolderId(),
            message.id,
            messagePatch,
            appState.state?.impersoniateUser?.userId
        )
        val result = call.execute()
        result.let { response ->
            if (response.isSuccessful) {
                return
            }
        }

        throw(RuntimeException())
    }

    override fun getStorageInfo(): StorageInfo {
        return api.getStorageInfo().execute().body() ?: throw(RuntimeException())
    }

    override fun getLatestUploads(offset: Int?, limit: Int?): List<Message> {
        return api.getUploads(offset, limit).execute().body() ?: throw(RuntimeException())
    }

    override fun uploadFileAsMessage(
        folderId: Int,
        filename: String,
        uriString: String,
        mimetype: String,
        callback: (Double) -> Unit
    ) {
        val url = "${Config.getApiUrl()}mail/folders/$folderId/messages"

        val body = CountingFileRequestBody(
            FilePickerUriHelper.getFile(context, uriString),
            mimetype,
            callback
        )

        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(filename, filename, body)
            // .addFormDataPart("filename", filename)
            .build()

        val request = Request.Builder()
            .url(url)
            .put(formBody)
            .build()
        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful)
            throw(RuntimeException())
    }


    override fun getPaymentDetails(folderId: Int, messageId: String): Payment {
        val call = api.getPaymentDetails(folderId, messageId)
        return call.execute().body() ?: throw RuntimeException()
    }

    override fun getPaymentLink(folderId: Int, messageId: String, type: String) : Link? {
        return api.getPaymentLink(folderId, messageId, type).execute().body()
    }
}