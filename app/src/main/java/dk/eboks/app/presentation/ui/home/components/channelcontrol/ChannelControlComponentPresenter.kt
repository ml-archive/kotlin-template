package dk.eboks.app.presentation.ui.home.components.channelcontrol

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelControlComponentPresenter @Inject constructor(
    private val getChannelHomeContentInteractor: GetChannelHomeContentInteractor
) :
    ChannelControlComponentContract.Presenter,
    BasePresenterImpl<ChannelControlComponentContract.View>(),
    GetChannelHomeContentInteractor.Output {
    override var continueFetching: Boolean = true

    init {
        getChannelHomeContentInteractor.output = this
    }

    override fun setup() {
        getChannelHomeContentInteractor.input = GetChannelHomeContentInteractor.Input(cached = true)
        getChannelHomeContentInteractor.run()
    }

    override fun refresh() {
        getChannelHomeContentInteractor.input =
            GetChannelHomeContentInteractor.Input(cached = false)
        getChannelHomeContentInteractor.run()
    }

    override fun onGetChannelHomeContent(channel: Channel, content: HomeContent) {
        runAction { v -> v.updateControl(channel, content.control) }
    }

    override fun onGetChannelHomeContentDone() {
        EventBus.getDefault().post(RefreshChannelControlDoneEvent())
    }

    override fun onGetInstalledChannelList(channels: MutableList<Channel>) {
        runAction { v ->
            v.showProgress(false)
            v.setupChannels(channels)
        }
    }

    override fun onGetInstalledChannelListError(error: ViewError) {
        Timber.e("onGetInstalledChannelListError")
        EventBus.getDefault().post(RefreshChannelControlDoneEvent())
        if (BuildConfig.DEBUG) // TODO Temp until backend is fixed
            runAction { v -> v.showErrorDialog(error) }
    }

    override fun onGetChannelHomeContentError(channel: Channel) {
        Timber.e("onGetChannelHomeContentError")
        runAction { v -> v.setControl(channel, Translation.home.errorContentFetch) }
    }

    override fun onGetChannelHomeContentEmpty(channel: Channel) {
        Timber.e("onGetChannelHomeContentEmpty")
        runAction { v -> v.setControl(channel, Translation.home.noContentText) }
    }

    override fun continueGetChannelHomeContent(): Boolean {
        return continueFetching
    }
}