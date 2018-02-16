package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderShortcutsComponentContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showFolders(folders : List<Folder>)

    }

    interface Presenter : BasePresenter<FolderShortcutsComponentContract.View> {
        fun openFolder(folder: Folder)
    }
}