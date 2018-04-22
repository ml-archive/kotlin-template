package dk.eboks.app.presentation.ui.components.home

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface HomeComponentContract {
    interface View : BaseView {
        var verifiedUser : Boolean

        fun setupChannels(channels : List<Channel>)
        fun setupChannelControl(channel : Channel, control : Control)
        fun showHighlights(messages : List<Message>)
        fun showProgress(show : Boolean)
        fun showRefreshProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun refresh()
        fun setup()
    }
}