package dk.eboks.app.presentation.ui.home.components.channelcontrol

import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.getType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
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
        getChannelHomeContentInteractor.input = GetChannelHomeContentInteractor.Input(cached = true)
        getChannelHomeContentInteractor.run()
    }

    override fun refresh() {
        getChannelHomeContentInteractor.input = GetChannelHomeContentInteractor.Input(cached = false)
        getChannelHomeContentInteractor.run()
    }

    override fun onGetPinnedChannelList(channels: MutableList<Channel>) {
        runAction { v ->
            v.showProgress(false)
            v.setupChannels(channels)
        }
    }

    override fun onGetChannelHomeContent(channel: Channel, content: HomeContent) {
        runAction { v -> v.updateControl(channel, content.control) }
    }

    override fun onGetChannelHomeContentDone() {
        EventBus.getDefault().post(RefreshChannelControlDoneEvent())
    }

    override fun onGetChannelHomeContentError(error: ViewError) {
        EventBus.getDefault().post(RefreshChannelControlDoneEvent())
        runAction { v->v.showErrorDialog(error) }
    }
}