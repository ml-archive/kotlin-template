package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderShortcutsComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<FolderShortcutsComponentContract.View> {
    }
}