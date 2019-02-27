package dk.eboks.app.presentation.ui.channels.components.overview

import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ChannelOverviewComponentPresenter @Inject constructor(
    private val getChannelsInteractor: GetChannelsInteractor
) :
    ChannelOverviewComponentContract.Presenter,
    BasePresenterImpl<ChannelOverviewComponentContract.View>(),
    GetChannelsInteractor.Output {

    init {
        getChannelsInteractor.output = this
    }

    override fun setup() {
        refresh(true)
    }

    override fun refresh(cached: Boolean) {
        Timber.d("Cached: $cached")
        getChannelsInteractor.input = GetChannelsInteractor.Input(cached)
        getChannelsInteractor.run()
    }

    override fun openChannel(channel: Channel) {
        /*
        appState.state?.channelState?.let { state ->
            state.selectedChannel = channel
        }
        */
        runAction { v -> v.showChannelOpening(channel) }
    }

    override fun onGetChannels(channels: List<Channel>) {
        runAction { v ->
            v.showChannels(channels)
            v.showProgress(false)
        }
    }

    override fun onGetChannelsError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}