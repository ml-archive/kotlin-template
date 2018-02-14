package dk.eboks.app.presentation.ui.components.folder.systemfolders

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SystemFoldersComponentPresenter @Inject constructor(val appState: AppStateManager) : SystemFoldersComponentContract.Presenter, BasePresenterImpl<SystemFoldersComponentContract.View>() {

    init {
    }

    override fun onViewCreated(view: SystemFoldersComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

    }

    override fun onViewDetached() {
        super.onViewDetached()
    }
}