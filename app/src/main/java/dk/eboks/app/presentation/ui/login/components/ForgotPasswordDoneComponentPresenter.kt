package dk.eboks.app.presentation.ui.login.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ForgotPasswordDoneComponentPresenter @Inject constructor(val appState: AppStateManager) : ForgotPasswordDoneComponentContract.Presenter, BasePresenterImpl<ForgotPasswordDoneComponentContract.View>() {

    init {
    }

}