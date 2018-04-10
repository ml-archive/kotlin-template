package dk.eboks.app.presentation.ui.screens.uploads

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class UploadsPresenter(val appStateManager: AppStateManager) : UploadsContract.Presenter, BasePresenterImpl<UploadsContract.View>() {
    init {
    }

}