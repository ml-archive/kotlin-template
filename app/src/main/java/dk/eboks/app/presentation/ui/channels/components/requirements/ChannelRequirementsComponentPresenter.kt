package dk.eboks.app.presentation.ui.channels.components.requirements

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.Requirement
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelRequirementsComponentPresenter @Inject constructor(val appState: AppStateManager) :
    ChannelRequirementsComponentContract.Presenter,
    BasePresenterImpl<ChannelRequirementsComponentContract.View>() {

    init {
    }

    private val unverifiedRequirements: ArrayList<Requirement> = ArrayList()

    override fun setup(channel: Channel) {
        buildUnverifiedRequirements(channel)
        runAction { v ->
            v.setupView(channel)
            v.showUnverifiedRequirements(unverifiedRequirements)
        }
    }

    private fun buildUnverifiedRequirements(channel: Channel) {
        channel.requirements?.let { reqs ->
            unverifiedRequirements.clear()
            unverifiedRequirements.addAll(reqs.filter { it.verified != null && it.verified == false })
        }
    }
}