package dk.eboks.app.presentation.ui.components.channels.content

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelContentComponentContract {
    interface View : BaseView {
        fun showChannel(channel : Channel)
        fun openChannelLink(link : Link)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(channel : Channel)
    }
}