package dk.eboks.app.presentation.ui.channels.components.settings

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelFlags
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelSettingsComponentContract {
    interface View : BaseView {
        fun setupChannel(channel: Channel)
        fun setCreditCards(cards: MutableList<StoreboxCreditCard>)
        fun showProgress(boolean: Boolean)
        fun showEmptyView(boolean: Boolean)
        fun setOnlyDigitalReceipts(onlyDigital: Boolean)
        fun showAddCardView(link : Link)
        fun broadcastCloseChannel()
        fun closeView()
    }

    interface Presenter : BasePresenter<View> {
        var currentChannel: Channel?
        fun setup(channelId : Int)
        fun getCreditCards()
        fun deleteCreditCard(id: String)
        fun getStoreboxProfile()
        fun saveStoreboxProfile(profile : StoreboxProfile)
        fun getStoreboxCardLink()
        fun deleteStoreboxAccountLink()
        fun updateChannelFlags(channel : Channel, flags : ChannelFlags)
        fun removeChannel()
    }
}