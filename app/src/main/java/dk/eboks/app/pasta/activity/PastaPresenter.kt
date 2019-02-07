package dk.eboks.app.pasta.activity

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PastaPresenter @Inject constructor(private val appStateManager: AppStateManager) :
    PastaContract.Presenter,
    BasePresenterImpl<PastaContract.View>() {
    init {
    }
}