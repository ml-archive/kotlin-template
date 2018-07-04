package dk.eboks.app.presentation.ui.channels.screens.content

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.presentation.ui.channels.components.settings.CloseChannelEvent
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class ChannelContentPresenter(val appStateManager: AppStateManager) : ChannelContentContract.Presenter, BasePresenterImpl<ChannelContentContract.View>() {
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
        runAction { v->v.finish() }
    }
}