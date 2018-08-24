package dk.eboks.app.network.managers

import android.content.Context
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import timber.log.Timber
import java.io.File


/**
 * Created by bison on 18-02-2018.
 */
class DownloadManagerImpl(val context: Context, val client: OkHttpClient, val cacheManager: FileCacheManager) : DownloadManager {
    init {

    }

    override fun downloadContent(message: Message, content: Content) : String? {
        Timber.e("Downloading content...")
        // TODO remove me
        //throw(ServerErrorException(ServerError(id = "ffsfws", type = ErrorType.ERROR, code = 12260)))
        var folderId = if(message.folder != null) message.folder!!.id else message.folderId
        var url = "${Config.getApiUrl()}mail/folders/$folderId/messages/${content.id}/content"
        content.contentUrlMock?.let { url = it } // if we have a mock url on the content object, use it instead

        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val filename = cacheManager.generateFileName(content)
        val downloadedFile = File(context.getCacheDir(), filename)
        // let okio do the actual buffering and writing of the file, after all thats why Jake made it, in his infinite wisdom
        val sink = Okio.buffer(Okio.sink(downloadedFile))
        sink.writeAll(response.body()!!.source())
        sink.close()
        Timber.e("Downloaded $url to $filename")
        return filename
    }

    override fun downloadAttachmentContent(message : Message, content: Content) : String? {
        Timber.e("Downloading attachment content...")
        var folderId = if(message.folder != null) message.folder!!.id else message.folderId
        var url = "${Config.getApiUrl()}mail/folders/$folderId/messages/${message.id}/attachment/${content.id}/content"
        content.contentUrlMock?.let { url = it } // if we have a mock url on the content object, use it instead

        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val filename = cacheManager.generateFileName(content)
        val downloadedFile = File(context.getCacheDir(), filename)
        // let okio do the actual buffering and writing of the file, after all thats why Jake made it, in his infinite wisdom
        val sink = Okio.buffer(Okio.sink(downloadedFile))
        sink.writeAll(response.body()!!.source())
        sink.close()
        Timber.e("Downloaded attachment $url to $filename")
        return filename
    }

    override fun downloadReceiptContent(receiptId : String) : String? {
        Timber.e("Downloading receipt content...")
        var url = "${Config.getApiUrl()}channels/storebox/receipts/$receiptId/content"

        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val filename = "tmpreceipt.pdf"
        val downloadedFile = File(context.getCacheDir(), filename)
        // let okio do the actual buffering and writing of the file, after all thats why Jake made it, in his infinite wisdom
        val sink = Okio.buffer(Okio.sink(downloadedFile))
        sink.writeAll(response.body()!!.source())
        sink.close()
        Timber.e("Downloaded receipt $url to $filename")
        return filename
    }

}