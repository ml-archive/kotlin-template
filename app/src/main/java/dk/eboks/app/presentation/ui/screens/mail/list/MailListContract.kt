package dk.eboks.app.presentation.ui.screens.mail.list

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MailListContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showFolderName(name : String)
    }

    interface Presenter : BasePresenter<View> {
    }
}