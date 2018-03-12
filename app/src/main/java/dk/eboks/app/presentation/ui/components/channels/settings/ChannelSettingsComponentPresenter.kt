package dk.eboks.app.presentation.ui.components.channels.settings

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelSettingsComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelSettingsComponentContract.Presenter, BasePresenterImpl<ChannelSettingsComponentContract.View>() {

    init {
    }

}