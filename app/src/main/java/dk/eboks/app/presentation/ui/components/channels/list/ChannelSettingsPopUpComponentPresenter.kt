package dk.eboks.app.presentation.ui.components.channels.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.pasta.fragment.PastaComponentContract
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelSettingsPopUpComponentPresenter @Inject constructor(val appState: AppStateManager) : ChannelSettingsPopUpComponentContract.Presenter, BasePresenterImpl<ChannelSettingsPopUpComponentContract.View>() {

    init {
    }

}