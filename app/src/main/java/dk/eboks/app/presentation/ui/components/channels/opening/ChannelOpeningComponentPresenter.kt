package dk.eboks.app.presentation.ui.components.channels.opening

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelOpeningComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelOpeningComponentContract.Presenter, BasePresenterImpl<ChannelOpeningComponentContract.View>() {

    init {
    }

}