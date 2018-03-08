package dk.eboks.app.presentation.ui.components.channels.overview

import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
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

    init {
        getChannelsInteractor.output = this
        refresh(true)

    }

    fun refresh(cached : Boolean)
    {
        getChannelsInteractor.input = GetChannelsInteractor.Input(cached)
        getChannelsInteractor.run()
    }

    override fun onGetChannels(channels: List<Channel>) {
        runAction { v->
            v.showChannels(channels)
        }
    }

    override fun onGetChannelsError(msg: String) {
        Timber.e(msg)
    }

}