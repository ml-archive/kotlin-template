package dk.eboks.app.presentation.ui.components.senders.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SenderAllListComponentPresenter @Inject constructor(val appState: AppStateManager) : SenderAllListComponentContract.Presenter, BasePresenterImpl<SenderAllListComponentContract.View>() {

    init {
    }

}