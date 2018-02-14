package dk.eboks.app.presentation.ui.components.folder.userfolders

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface UserFoldersComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<UserFoldersComponentContract.View> {
    }
}