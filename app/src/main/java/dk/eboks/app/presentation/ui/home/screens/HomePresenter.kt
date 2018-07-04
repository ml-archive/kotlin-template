package dk.eboks.app.presentation.ui.home.screens

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class HomePresenter(val appStateManager: AppStateManager) : HomeContract.Presenter, BasePresenterImpl<HomeContract.View>() {
    init {
    }

    override fun setup() {
        runAction { v->
            v.addFolderPreviewComponentFragment(Folder(type = FolderType.HIGHLIGHTS))
            v.addChannelControlComponentFragment()
        }
    }

}