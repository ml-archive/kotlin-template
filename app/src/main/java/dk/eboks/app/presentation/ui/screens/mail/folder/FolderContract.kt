package dk.eboks.app.presentation.ui.screens.mail.folder

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}