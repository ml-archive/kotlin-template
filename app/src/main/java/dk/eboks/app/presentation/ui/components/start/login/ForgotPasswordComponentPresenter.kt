package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ForgotPasswordComponentPresenter @Inject constructor(val appState: AppStateManager) : ForgotPasswordComponentContract.Presenter, BasePresenterImpl<ForgotPasswordComponentContract.View>() {

    init {
    }

}