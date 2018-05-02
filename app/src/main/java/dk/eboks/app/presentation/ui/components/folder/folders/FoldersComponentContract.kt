package dk.eboks.app.presentation.ui.components.folder.folders

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FoldersComponentContract {
    interface View : ComponentBaseView {
        fun getModeType():FolderMode
        fun showUserFolders(folders : List<Folder>)
        fun showSystemFolders(folders : List<Folder>)
        fun showRefreshProgress(show : Boolean)
        fun showSelectFolders(folders : List<Folder>)
    }

    interface Presenter : BasePresenter<FoldersComponentContract.View> {
        fun openFolder(folder: Folder)
        fun refresh()
    }
}