package dk.eboks.app.presentation.ui.components.home.channelcontrol

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelControlComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelControlComponentContract.Presenter, BasePresenterImpl<ChannelControlComponentContract.View>() {

    init {
    }

}