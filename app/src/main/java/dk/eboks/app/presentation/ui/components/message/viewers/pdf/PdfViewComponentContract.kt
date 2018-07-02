package dk.eboks.app.presentation.ui.components.message.viewers.pdf

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface PdfViewComponentContract {
    interface View : BaseView {
        fun updateView(folder : Folder)
    }

    interface Presenter : BasePresenter<View> {
    }
}