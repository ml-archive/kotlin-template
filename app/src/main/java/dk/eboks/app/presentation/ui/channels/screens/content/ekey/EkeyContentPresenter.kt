package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class EkeyContentPresenter(val appStateManager: AppStateManager) : EkeyContentContract.Presenter, BasePresenterImpl<EkeyContentContract.View>() {
    init {
    }

}