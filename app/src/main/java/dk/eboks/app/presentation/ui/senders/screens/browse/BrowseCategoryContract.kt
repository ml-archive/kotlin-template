package dk.eboks.app.presentation.ui.senders.screens.browse

import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseView
import dk.eboks.app.presentation.base.ComponentBaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 * Copied by chnt on 12-03-2018
 */
interface BrowseCategoryContract {
    interface View : BaseView, ComponentBaseView {
        fun showSenders(senders: List<Sender>)
        fun showError(msg: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadSenders(senderId : Long)
        fun searchSenders(searchText : String)
    }
}