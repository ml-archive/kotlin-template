package dk.eboks.app.mail.presentation.ui.screens.overview

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.RefreshFolderShortcutsDoneEvent
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.RefreshFolderShortcutsEvent
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.RefreshSenderCarouselDoneEvent
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.RefreshSenderCarouselEvent
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class MailOverviewPresenter @Inject constructor(private val appState: AppStateManager) :
    MailOverviewContract.Presenter,
    BasePresenterImpl<MailOverviewContract.View>() {
    private var refreshingFolders = false
    private var refreshingSenders = false

    init {
        runAction(this::setUser)
    }

    override fun onViewCreated(view: MailOverviewContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        EventBus.getDefault().unregister(this)
        super.onViewDetached()
    }

    override fun refresh() {
        refreshingFolders = true
        refreshingSenders = true
        EventBus.getDefault().post(RefreshFolderShortcutsEvent())
        EventBus.getDefault().post(RefreshSenderCarouselEvent())

        // Reset shared user
        appState.state?.impersoniateUser = null
        runAction { view ->
            setUser(view)
        }
    }

    private fun setUser(view: MailOverviewContract.View) {
        view.setUser(appState.state?.currentUser, appState.state?.currentUser?.name)
    }

    private fun stopProgressIfDone() {
        if (!refreshingFolders && !refreshingSenders)
            runAction { v -> v.showProgress(false) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshFolderShortcutsDoneEvent) {
        refreshingFolders = false
        stopProgressIfDone()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshSenderCarouselDoneEvent) {
        refreshingSenders = false
        stopProgressIfDone()
    }
}