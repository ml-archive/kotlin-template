package dk.eboks.app.presentation.ui.components.folder.folders

import dk.eboks.app.domain.models.Folder
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FoldersComponentContract {
    interface View : ComponentBaseView {
        fun showUserFolders(folders : List<Folder>)
        fun showSystemFolders(folders : List<Folder>)
        fun showError(msg : String)
        fun showRefreshProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<FoldersComponentContract.View> {
        fun openFolder(folder: Folder)
        fun refresh()
    }
}