package dk.eboks.app.presentation.ui.components.folder.folders.newfolder

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface NewFolderComponentContract {
    interface View : BaseView {
        fun setRootFolder(user : String)
    }

    interface Presenter : BasePresenter<View> {
    }
}