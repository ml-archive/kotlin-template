package dk.eboks.app.presentation.ui.components.channels.opening

import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelOpeningComponentContract {
    interface View : BaseView {
        fun showOpenState(channel : Channel)
        fun showDisabledState(channel: Channel)
        fun showInstallState(channel: Channel)
        fun showVerifyState(channel: Channel, provider : LoginProvider)
        fun showProgress(show : Boolean)
        fun openChannelContent()
        fun openStoreBoxContent()
    }

    interface Presenter : BasePresenter<View> {
        fun install(channel : Channel, provider : LoginProvider)
        fun open(channel: Channel)
    }
}