package dk.eboks.app.presentation.ui.components.channels.list

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelListComponentContract {
    interface View : BaseView {
        fun showChannels(channels : List<Channel>)
    }

    interface Presenter : BasePresenter<View> {
    }
}