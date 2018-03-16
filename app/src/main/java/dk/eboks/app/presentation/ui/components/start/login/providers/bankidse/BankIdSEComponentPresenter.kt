package dk.eboks.app.presentation.ui.components.start.login.providers.bankidse

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class BankIdSEComponentPresenter @Inject constructor(val appState: AppStateManager) : BankIdSEComponentContract.Presenter, BasePresenterImpl<BankIdSEComponentContract.View>() {

    init {

    }

}