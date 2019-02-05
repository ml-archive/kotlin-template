package dk.eboks.app.presentation.ui.folder.screens

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderPresenter @Inject constructor(val appState: AppStateManager) :
    FolderContract.Presenter,
    BasePresenterImpl<FolderContract.View>() {
    init {
    }
}