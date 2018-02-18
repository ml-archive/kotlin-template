package dk.eboks.app.presentation.ui.components.mail.sendercarousel

import dk.eboks.app.domain.models.Sender
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SenderCarouselComponentContract {
    interface View : ComponentBaseView {
        fun showSenders(senders : List<Sender>)
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<SenderCarouselComponentContract.View> {
    }
}