package dk.eboks.app.presentation.ui.components.channels.settings

import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelSettingsComponentContract {
    interface View : BaseView {
        fun setCreditCards(cards: MutableList<StoreboxCreditCard>)
        fun showProgress(boolean: Boolean)
        fun showEmptyView(boolean: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun getCreditCards()
        fun deleteCreditCard(id: String)
    }
}