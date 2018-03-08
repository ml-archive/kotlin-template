package dk.eboks.app.presentation.ui.components.channels.requirements

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelRequirementsComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelRequirementsComponentContract.Presenter, BasePresenterImpl<ChannelRequirementsComponentContract.View>() {

    init {
    }

}