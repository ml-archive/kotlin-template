package dk.eboks.app.presentation.ui.components.signup

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SignupComponentPresenter @Inject constructor(val appState: AppStateManager) : SignupComponentContract.Presenter, BasePresenterImpl<SignupComponentContract.SignupView>() {

    init {
    }

}