package dk.eboks.app.presentation.ui.channels.screens.content

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.ui.channels.components.settings.CloseChannelEvent
import dk.eboks.app.util.getType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelContentPresenter @Inject constructor() :
    ChannelContentContract.Presenter, BasePresenterImpl<ChannelContentContract.View>() {
    init {
    }

    override fun onViewCreated(view: ChannelContentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        EventBus.getDefault().unregister(this)
        super.onViewDetached()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CloseChannelEvent) {
        Timber.e("ChannelContentPresenter getting CloseChannelEvent")
        runAction { v -> v.finish() }
    }

    override fun open(channel: Channel) {
        // storebox channels id 1 - 3
        // ekey channels id 101 - 103

        when (channel.getType()) {
            "channel" -> {
                runAction { v ->
                    v.openChannelContent(channel)
                }
            }
            "storebox" -> {
                runAction { v ->
                    v.openStoreBoxContent(channel)
                }
            }
            "ekey" -> {
            }
        }
    }
}