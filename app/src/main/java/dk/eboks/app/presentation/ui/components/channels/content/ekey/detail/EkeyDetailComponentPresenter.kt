package dk.eboks.app.presentation.ui.components.channels.content.ekey.detail

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyDetailComponentPresenter @Inject constructor(val appState: AppStateManager) : EkeyDetailComponentContract.Presenter, BasePresenterImpl<EkeyDetailComponentContract.View>() {

    init {
    }

}