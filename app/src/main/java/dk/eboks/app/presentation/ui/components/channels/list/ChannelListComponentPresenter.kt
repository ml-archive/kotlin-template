package dk.eboks.app.presentation.ui.components.channels.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelListComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelListComponentContract.Presenter, BasePresenterImpl<ChannelListComponentContract.View>() {

    init {
    }

}