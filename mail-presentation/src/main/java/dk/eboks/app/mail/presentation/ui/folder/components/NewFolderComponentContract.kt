package dk.eboks.app.mail.presentation.ui.folder.components

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface NewFolderComponentContract {
    interface View : BaseView {
        fun setRootFolder(user: String)
        fun showFolderNameError()
        fun finish()
        val overrideActiveUser: Boolean
    }

    interface Presenter : BasePresenter<View> {
        fun createNewFolder(parentFolderId: Int, name: String)
        fun folderNameNotAllowed()
        fun deleteFolder(folderId: Int)
        fun editFolder(folderId: Int, parentFolderId: Int?, name: String?)
    }
}