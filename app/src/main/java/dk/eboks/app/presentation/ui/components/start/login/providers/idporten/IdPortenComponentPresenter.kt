package dk.eboks.app.presentation.ui.components.start.login.providers.idporten

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class IdPortenComponentPresenter @Inject constructor(val appState: AppStateManager) : IdPortenComponentContract.Presenter, BasePresenterImpl<IdPortenComponentContract.View>() {

    init {

    }

}