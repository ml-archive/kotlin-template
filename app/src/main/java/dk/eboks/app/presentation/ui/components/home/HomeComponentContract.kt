package dk.eboks.app.presentation.ui.components.home

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface HomeComponentContract {
    interface View : BaseView {
        var verifiedUser : Boolean
        fun setupViews()
        fun setupChannels(channels : List<Channel>)
        fun setupChannelControl(control : Control)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
    }
}