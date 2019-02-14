package dk.eboks.app.presentation.ui.mail.components.sendercarousel

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.sender.GetSendersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SenderCarouselComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getSendersInteractor: GetSendersInteractor
) :
    SenderCarouselComponentContract.Presenter,
    BasePresenterImpl<SenderCarouselComponentContract.View>(),
    GetSendersInteractor.Output {

    init {
        refresh(true)
        runAction { v -> v.showProgress(true) }
    }

    override fun onViewCreated(view: SenderCarouselComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        EventBus.getDefault().unregister(this)
        super.onViewDetached()
    }

    fun refresh(cached: Boolean) {
        getSendersInteractor.input = GetSendersInteractor.Input(cached, userId = null)
        getSendersInteractor.output = this
        getSendersInteractor.run()
    }

    override fun onGetSenders(senders: List<Sender>) {
        // Timber.e("Received them senders")
        val verified = appState.state?.currentUser?.verified ?: false
        runAction { v ->
            v.showProgress(false)
            EventBus.getDefault().post(RefreshSenderCarouselDoneEvent())
            if (senders.isNotEmpty()) {
                v.showEmpty(false, verified)

                v.showSenders(sortSenders(senders))
            } else {
                v.showEmpty(true, verified)
            }
        }
    }

    private fun sortSenders(senders: List<Sender>): List<Sender> {
        return senders.sortedWith(compareBy({ it.unreadMessageCount <= 0 }, { it.name }))
    }

    override fun onGetSendersError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            EventBus.getDefault().post(RefreshSenderCarouselDoneEvent())
            v.showErrorDialog(error)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshSenderCarouselEvent) {
        refresh(false)
    }
}