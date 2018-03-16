package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NemIdComponentPresenter @Inject constructor(val appState: AppStateManager) : NemIdComponentContract.Presenter, BasePresenterImpl<NemIdComponentContract.View>() {

    init {

    }

}