package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MailListComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<MailListComponentContract.View> {
    }
}