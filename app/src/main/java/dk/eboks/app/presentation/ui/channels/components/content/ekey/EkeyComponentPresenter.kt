package dk.eboks.app.presentation.ui.channels.components.content.ekey

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyComponentPresenter @Inject constructor(val appState: AppStateManager) : EkeyComponentContract.Presenter, BasePresenterImpl<EkeyComponentContract.View>() {

    init {
    }

}