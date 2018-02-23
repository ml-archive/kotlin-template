package dk.eboks.app.presentation.ui.screens.channels

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class ChannelsPresenter(val appStateManager: AppStateManager) :
        ChannelsContract.Presenter,
        BasePresenterImpl<ChannelsContract.View>()
{
    init {
    }

}