package dk.eboks.app.presentation.ui.uploads.screens.fileupload

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FileUploadPresenter @Inject constructor(private val appState: AppStateManager) :
    FileUploadContract.Presenter, BasePresenterImpl<FileUploadContract.View>() {

    private var uriString: String? = null
    private var mimeType: String? = null

    override fun setup(uriString: String, mimeType: String?) {
        Timber.e("Got uriString $uriString mimeType = $mimeType")

        this.uriString = uriString
        this.mimeType = mimeType

        startViewer(uriString, mimeType)
        runAction { v ->
            v.showFilename(uriString)
            getDefaultFolder()?.let { deffolder ->
                v.showDestinationFolder(deffolder)
            }
        }
    }

    private fun getDefaultFolder(): Folder? {
        appState.state?.selectedFolders?.let { folders ->
            for (f in folders) {
                if (f.type == FolderType.INBOX) {
                    return f
                }
            }
        }
        return null
    }

    fun startViewer(uriString: String, mimetype: String?) {
        if (mimetype?.startsWith("image/", true) == true) {
            runAction { v -> v.addImageViewer(uriString) }
            return
        }
        if (mimetype == "text/html") {
            runAction { v -> v.addHtmlViewer(uriString) }
            return
        }
        if (mimetype?.startsWith("text/", true) == true) {
            runAction { v -> v.addTextViewer(uriString) }
            return
        }
        // default
        runAction { v -> v.showNoPreviewAvailable() }
    }

    override fun isVerified(): Boolean {
        appState.state?.currentUser?.let {
            return it.verified
        }
        return false
    }
}