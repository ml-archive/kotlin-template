package dk.eboks.app.presentation.ui.uploads.screens.fileupload

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FileUploadContract {
    interface View : BaseView {
        fun showFilename(uriString: String)
        fun showDestinationFolder(folder: Folder)
        fun addPdfViewer(uri: String)
        fun addImageViewer(uri: String)
        fun addHtmlViewer(uri: String)
        fun addTextViewer(uri: String)
        fun showNoPreviewAvailable()
        fun setHighPeakHeight()
    }

    interface Presenter : BasePresenter<View> {
        fun setup(uriString: String, mimeType: String?)
        fun isVerified(): Boolean
    }
}