package dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface PdfViewComponentContract {
    interface View : BaseView {
        fun updateView(folder: Folder)
        fun showPdfView(filename: String)
    }

    interface Presenter : BasePresenter<View> {

        val currentFile: String?
    }
}