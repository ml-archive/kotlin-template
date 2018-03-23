package dk.eboks.app.presentation.ui.components.channels.overview

import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelOverviewComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelsInteractor: GetChannelsInteractor) :
        ChannelOverviewComponentContract.Presenter,
        BasePresenterImpl<ChannelOverviewComponentContract.View>(),
        GetChannelsInteractor.Output
{

    val channels = appState.state?.channelState

    init {
        getChannelsInteractor.output = this
        refresh(true)

    }

    fun refresh(cached : Boolean)
    {
        getChannelsInteractor.input = GetChannelsInteractor.Input(cached)
        getChannelsInteractor.run()
    }

    override fun openChannel(channel: Channel) {
        appState.state?.channelState?.selectedChannel = channel
        appState.state?.channelState?.openOrInstallImmediately = false
        runAction { v-> v.showChannelOpening() }
    }

    override fun install(channel: Channel) {
        appState.state?.channelState?.selectedChannel = channel
        appState.state?.channelState?.openOrInstallImmediately = true
        runAction { v-> v.showChannelOpening() }
    }

    override fun open(channel: Channel) {
        appState.state?.channelState?.selectedChannel = channel
        appState.state?.channelState?.openOrInstallImmediately = true
        runAction { v-> v.showChannelOpening() }
    }

    override fun onGetChannels(channels: List<Channel>) {
        runAction { v->
            v.showChannels(channels)
            v.showProgress(false)
        }
    }

    override fun onGetChannelsError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun refresh() {
        channels?.let{
            getChannelsInteractor.input = GetChannelsInteractor.Input(false)
            getChannelsInteractor.run()
        }
    }
}