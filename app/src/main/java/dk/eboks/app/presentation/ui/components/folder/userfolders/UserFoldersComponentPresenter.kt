package dk.eboks.app.presentation.ui.components.folder.userfolders

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class UserFoldersComponentPresenter @Inject constructor(val appState: AppStateManager) : UserFoldersComponentContract.Presenter, BasePresenterImpl<UserFoldersComponentContract.View>() {

    init {
    }

}