package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderShortcutsComponentPresenter @Inject constructor(val appState: AppStateManager) : FolderShortcutsComponentContract.Presenter, BasePresenterImpl<FolderShortcutsComponentContract.View>() {

    init {
    }

}