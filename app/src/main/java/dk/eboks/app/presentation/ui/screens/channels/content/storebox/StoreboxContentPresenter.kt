package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class StoreboxContentPresenter(val appStateManager: AppStateManager) : StoreboxContentContract.Presenter, BasePresenterImpl<StoreboxContentContract.View>() {
    init {
    }

}