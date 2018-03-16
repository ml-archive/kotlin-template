package dk.eboks.app.presentation.ui.components.senders

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
class SenderGroupsComponentPresenter @Inject constructor(val appState: AppStateManager) : SenderGroupsComponentContract.Presenter, BasePresenterImpl<SenderGroupsComponentContract.View>() {

    init {
    }

    override fun getSenderGroups(senderID: Long) {
        runAction { v ->
            v.showSenderGroups(ArrayList())
        }
    }

}