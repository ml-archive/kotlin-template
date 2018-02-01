package dk.eboks.app.presentation.ui.mail

import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MailOverviewContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showSenders(senders : List<Sender>)
    }

    interface Presenter : BasePresenter<MailOverviewContract.View> {
    }
}