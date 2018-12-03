package dk.eboks.app.presentation.ui.home.components.channelcontrol

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelControlComponentContract {
    interface View : BaseView {
        fun setupChannels(channels : MutableList<Channel>)
        fun updateControl(channel : Channel, control : Control)
        fun setControl(channel : Channel, text : String)
        fun showProgress(show : Boolean)
        fun isDetached(): Boolean
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun refresh()
        var continueFetching: Boolean
    }
}