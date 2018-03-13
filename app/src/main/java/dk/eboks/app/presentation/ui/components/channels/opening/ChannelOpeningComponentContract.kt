package dk.eboks.app.presentation.ui.components.channels.opening

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelOpeningComponentContract {
    interface View : BaseView {
        fun showOpenState(channel : Channel)
        fun showDisabledState(channel: Channel)
        fun showInstallState(channel: Channel)
    }

    interface Presenter : BasePresenter<View> {
    }
}