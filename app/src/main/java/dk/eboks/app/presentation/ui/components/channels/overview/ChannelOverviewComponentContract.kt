package dk.eboks.app.presentation.ui.components.channels.overview

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelOverviewComponentContract {
    interface View : BaseView {
        fun showChannels(channels : List<Channel>)
    }

    interface Presenter : BasePresenter<View> {
    }
}