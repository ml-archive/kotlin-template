package dk.eboks.app.presentation.ui.folder.components.newfolder

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface NewFolderComponentContract {
    interface View : BaseView {
        fun setRootFolder(user : String)
        fun showFolderNameError()
        fun finsish()
    }

    interface Presenter : BasePresenter<View> {
        fun createNewFolder(parentFolderId : Int, name: String)
        fun folderNameNotAllowed()
        fun deleteFolder(folderiId: Int)
        fun editFolder(folderiId: Int, parentFolderId: Int?, name: String?)
    }
}