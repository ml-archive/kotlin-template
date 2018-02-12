package dk.eboks.app.presentation.ui.message.components.pdfpreview

import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface PdfPreviewComponentContract {
    interface View : BaseView {
        fun updateView(folder : Folder)
    }

    interface Presenter : BasePresenter<PdfPreviewComponentContract.View> {
    }
}