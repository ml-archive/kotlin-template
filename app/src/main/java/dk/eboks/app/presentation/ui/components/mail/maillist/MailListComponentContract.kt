package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.models.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MailListComponentContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showRefreshProgress(show : Boolean)
        fun showMessages(messages : List<Message>)
    }

    interface Presenter : BasePresenter<MailListComponentContract.View> {
        fun setCurrentMessage(message: Message)
        fun refresh()
    }
}