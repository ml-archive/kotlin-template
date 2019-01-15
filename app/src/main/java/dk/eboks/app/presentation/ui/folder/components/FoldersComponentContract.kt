package dk.eboks.app.presentation.ui.folder.components

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FoldersComponentContract {
    interface View : ComponentBaseView {
        fun getModeType(): FolderMode
        fun showUserFolders(folders : List<Folder>)
        fun showSystemFolders(folders : List<Folder>)
        fun showRefreshProgress(show : Boolean)
        fun showSelectFolders(folders : List<Folder>)
        fun setUser(user: User?)

        val isSharedUserActive: Boolean
    }

    interface Presenter : BasePresenter<View> {
        fun openFolder(folder: Folder)
        fun refresh()

    }
}