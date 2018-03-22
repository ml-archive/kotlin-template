package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FolderShortcutsComponentContract {
    interface View : ComponentBaseView {
        fun showFolders(folders : List<Folder>)

    }

    interface Presenter : BasePresenter<FolderShortcutsComponentContract.View> {
        fun openFolder(folder: Folder)
    }
}