package dk.eboks.app.presentation.ui.components.profile.drawer

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EmailVerificationComponentPresenter @Inject constructor(val appState: AppStateManager) : EmailVerificationComponentContract.Presenter, BasePresenterImpl<EmailVerificationComponentContract.View>() {

    init {
    }

}