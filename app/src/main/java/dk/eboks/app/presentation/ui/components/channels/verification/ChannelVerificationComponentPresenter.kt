package dk.eboks.app.presentation.ui.components.channels.verification

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelVerificationComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelVerificationComponentContract.Presenter, BasePresenterImpl<ChannelVerificationComponentContract.View>() {

    init {
    }

}