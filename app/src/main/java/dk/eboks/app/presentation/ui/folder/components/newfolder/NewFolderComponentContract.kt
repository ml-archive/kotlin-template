package dk.eboks.app.presentation.ui.folder.components.newfolder

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

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