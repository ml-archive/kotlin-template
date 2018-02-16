package dk.eboks.app.presentation.ui.components.mail.sendercarousel

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.GetSendersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SenderCarouselComponentPresenter @Inject constructor(val appState: AppStateManager, val getSendersInteractor: GetSendersInteractor) :
        SenderCarouselComponentContract.Presenter,
        BasePresenterImpl<SenderCarouselComponentContract.View>(),
        GetSendersInteractor.Output {

    init {
        refresh(true)
    }

    override fun onViewCreated(view: SenderCarouselComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        EventBus.getDefault().unregister(this)
        super.onViewDetached()
    }

    fun refresh(cached : Boolean)
    {
        getSendersInteractor.input = GetSendersInteractor.Input(cached)
        getSendersInteractor.output = this
        getSendersInteractor.run()
    }

    override fun onGetSenders(senders: List<Sender>) {
        Timber.e("Received them senders")
        runAction { v ->
            EventBus.getDefault().post(RefreshSenderCarouselDoneEvent())
            v.showSenders(senders)
        }
    }

    override fun onGetSendersError(msg: String) {
        Timber.e(msg)
        runAction { EventBus.getDefault().post(RefreshSenderCarouselDoneEvent()) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshSenderCarouselEvent) {
        refresh(false)
    }
}