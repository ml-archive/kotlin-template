package dk.eboks.app.presentation.ui.screens.senders.overview

import dk.eboks.app.domain.models.Sender
import dk.eboks.app.domain.models.SenderCategory
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 * Copied by chnt on 12-03-2018
 */
interface BrowseCategoryContract {
    interface View : BaseView {
        fun showSenders(categories: List<Sender>)
        fun showError(msg: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadSenders(senderId : Long)
    }
}