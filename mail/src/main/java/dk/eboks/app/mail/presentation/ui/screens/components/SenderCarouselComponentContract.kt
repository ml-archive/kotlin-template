package dk.eboks.app.mail.presentation.ui.screens.components

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface SenderCarouselComponentContract {
    interface View : ComponentBaseView {
        fun showSenders(senders: List<Sender>)
        fun showEmpty(show: Boolean, verified: Boolean)
    }

    interface Presenter : BasePresenter<View>
}