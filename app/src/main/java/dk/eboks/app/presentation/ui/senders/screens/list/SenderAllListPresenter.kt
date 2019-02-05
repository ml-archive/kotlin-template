package dk.eboks.app.presentation.ui.senders.screens.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class SenderAllListPresenter(val appStateManager: AppStateManager) :
    SenderAllListContract.Presenter, BasePresenterImpl<SenderAllListContract.View>() {
    init {
    }
}