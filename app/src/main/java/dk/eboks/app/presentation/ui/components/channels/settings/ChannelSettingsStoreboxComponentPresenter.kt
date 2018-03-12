package dk.eboks.app.presentation.ui.components.channels.settings

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelSettingsStoreboxComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelSettingsStoreboxComponentContract.Presenter, BasePresenterImpl<ChannelSettingsStoreboxComponentContract.View>() {

    init {
    }

}