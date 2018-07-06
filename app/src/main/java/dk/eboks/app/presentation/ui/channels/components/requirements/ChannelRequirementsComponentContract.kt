package dk.eboks.app.presentation.ui.channels.components.requirements

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.Requirement
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelRequirementsComponentContract {
    interface View : BaseView {
        fun setupView(channel: Channel)
        fun showUnverifiedRequirements(requirements : List<Requirement>)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(channel : Channel)
    }
}