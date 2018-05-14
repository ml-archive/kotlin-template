package dk.eboks.app.presentation.ui.components.home.channelcontrol

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelControlComponentContract {
    interface View : BaseView {
        fun setupChannels(channels : MutableList<Channel>)
        fun showProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
    }
}