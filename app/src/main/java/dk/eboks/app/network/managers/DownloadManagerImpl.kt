package dk.eboks.app.network.managers

import android.content.Context
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.models.Content
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

    override fun downloadContent(content: Content) : String? {
        try {
            Timber.e("Downloading content...")
            var url = "${BuildConfig.MOCK_API_URL}/mail/folders/1/messages/${content.id}/content"
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
        catch (t : Throwable)
        {
            t.printStackTrace()
        }
        return null
    }


}