package dk.eboks.app.presentation.ui.senders.components.list

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface SenderAllListComponentContract {
    interface View : ComponentBaseView {
        fun showSenders(senders: List<Sender>)
    }

    interface Presenter : BasePresenter<View> {
        fun refresh()
        fun loadAllSenders()
        fun searchSenders(searchText: String)
    }
}