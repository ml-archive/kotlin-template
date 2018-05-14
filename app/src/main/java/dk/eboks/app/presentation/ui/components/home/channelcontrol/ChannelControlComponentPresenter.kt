package dk.eboks.app.presentation.ui.components.home.channelcontrol

import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelControlComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelHomeContentInteractor: GetChannelHomeContentInteractor) :
        ChannelControlComponentContract.Presenter,
        BasePresenterImpl<ChannelControlComponentContract.View>(),
        GetChannelHomeContentInteractor.Output
{

    init {
        getChannelHomeContentInteractor.output = this
    }

    override fun setup() {
        Timber.e("Setup")
    }

    override fun onGetPinnedChannelList(channels: MutableList<Channel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetChannelHomeContent(channel: Channel, content: HomeContent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetChannelHomeContentError(error: ViewError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}