package dk.eboks.app.presentation.ui.channels.components.requirements

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ChannelRequirementsComponentPresenter @Inject constructor() :
    ChannelRequirementsComponentContract.Presenter,
    BasePresenterImpl<ChannelRequirementsComponentContract.View>() {

    override fun setup(channel: Channel) {
        view {
            setupView(channel)
            showUnverifiedRequirements(channel.requirements
                ?.filter { it.verified != null && it.verified == false }
                ?: listOf())
        }
    }
}