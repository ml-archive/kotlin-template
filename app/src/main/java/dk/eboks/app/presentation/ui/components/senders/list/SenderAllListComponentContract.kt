package dk.eboks.app.presentation.ui.components.senders.list

import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView
import dk.eboks.app.presentation.base.ComponentBaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SenderAllListComponentContract {
    interface View : ComponentBaseView {
        fun showSenders(senders : List<Sender>)

    }

    interface Presenter : BasePresenter<SenderAllListComponentContract.View> {
        fun refresh()
        fun loadAllSenders()
        fun searchSenders(searchText : String)
    }
}