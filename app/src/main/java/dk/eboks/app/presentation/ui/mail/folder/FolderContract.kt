package dk.eboks.app.presentation.ui.mail.folder

import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showRefreshProgress(show : Boolean)
        fun showSystemFolders(folders : List<Folder>)
        fun showUserFolders(folders : List<Folder>)
    }

    interface Presenter : BasePresenter<View> {
        fun refresh()
    }
}