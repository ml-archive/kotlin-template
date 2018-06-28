package dk.eboks.app.presentation.ui.components.home.folderpreview

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderPreviewComponentContract {
    interface View : BaseView {
        fun showEmptyState(show : Boolean, verifiedUser : Boolean)
        fun showFolder(messages : List<Message>, verifiedUser : Boolean)
        fun showProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(folder : Folder)
        fun refresh(cached : Boolean)
    }
}