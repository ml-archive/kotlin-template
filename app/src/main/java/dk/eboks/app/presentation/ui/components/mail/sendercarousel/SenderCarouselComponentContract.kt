package dk.eboks.app.presentation.ui.components.mail.sendercarousel

import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SenderCarouselComponentContract {
    interface View : BaseView {
        fun showSenders(senders : List<Sender>)
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<SenderCarouselComponentContract.View> {
    }
}