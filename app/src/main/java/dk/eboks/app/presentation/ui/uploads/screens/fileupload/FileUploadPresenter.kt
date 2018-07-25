package dk.eboks.app.presentation.ui.uploads.screens.fileupload

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FileUploadPresenter @Inject constructor(val appState: AppStateManager) : FileUploadContract.Presenter, BasePresenterImpl<FileUploadContract.View>() {

    override fun setup(uriString : String, mimeType : String?) {
        Timber.e("Got uriString $uriString mimeType = $mimeType")

        startViewer(uriString, mimeType)
        runAction { v ->

        }
    }

    fun startViewer(uriString : String, mimetype : String?)
    {
        if(mimetype?.startsWith("image/", true) == true)
        {
            runAction { v-> v.addImageViewer(uriString) }
            return
        }
        if(mimetype == "text/html")
        {
            runAction { v-> v.addHtmlViewer(uriString) }
            return
        }
        if(mimetype?.startsWith("text/", true) == true)
        {
            runAction { v-> v.addTextViewer(uriString) }
            return
        }
        // default
        runAction { v->v.showNoPreviewAvailable() }
    }
}