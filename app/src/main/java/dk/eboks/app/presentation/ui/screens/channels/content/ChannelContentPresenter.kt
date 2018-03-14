package dk.eboks.app.presentation.ui.screens.channels.content

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class ChannelContentPresenter(val appStateManager: AppStateManager) : ChannelContentContract.Presenter, BasePresenterImpl<ChannelContentContract.View>() {
    init {
    }

}