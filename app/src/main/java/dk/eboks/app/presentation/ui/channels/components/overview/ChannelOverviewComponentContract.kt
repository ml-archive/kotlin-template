package dk.eboks.app.presentation.ui.channels.components.overview

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelOverviewComponentContract {
    interface View : BaseView {
        fun showChannels(channels : List<Channel>)
        fun showChannelOpening(channel: Channel)
        fun showProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun openChannel(channel : Channel)
        fun refresh(cached : Boolean)
    }
}