package dk.eboks.app.pasta

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PastaComponentPresenter @Inject constructor(val appState: AppStateManager) : PastaComponentContract.Presenter, BasePresenterImpl<PastaComponentContract.View>() {

    init {
    }

}