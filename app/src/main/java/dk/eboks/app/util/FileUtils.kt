package dk.eboks.app.util

import android.content.Context
import android.content.Intent
import android.support.v4.content.FileProvider
import dk.eboks.app.BuildConfig
import timber.log.Timber
import java.io.File

/**
 * Created by bison on 21/02/18.
 */
object FileUtils {
    fun openExternalViewer(cur_context : Context, filename: String, mimeType : String) {
        //handler.post {
        Timber.e("Opening document $filename $mimeType")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri = FileProvider.getUriForFile(cur_context, BuildConfig.APPLICATION_ID + ".fileprovider", File(filename))
        intent.setDataAndType(uri, mimeType)
        Timber.e("URI $uri")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (intent.resolveActivity(cur_context.packageManager) != null) {
            cur_context.startActivity(Intent.createChooser(intent, "_Open with"))
        } else {
            Timber.e("Could not resolve share intent")
        }

        //}
    }
}