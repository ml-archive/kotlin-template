package dk.eboks.app.presentation.ui.components.verification

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class VerificationComponentPresenter @Inject constructor(val appState: AppStateManager) : VerificationComponentContract.Presenter, BasePresenterImpl<VerificationComponentContract.View>() {

    init {
    }

}