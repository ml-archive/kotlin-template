package dk.eboks.app.presentation.ui.components.folder.systemfolders

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SystemFoldersComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<SystemFoldersComponentContract.View> {
    }
}