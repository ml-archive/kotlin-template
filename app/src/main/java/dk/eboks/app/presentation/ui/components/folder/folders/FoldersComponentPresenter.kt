package dk.eboks.app.presentation.ui.components.folder.folders

import dk.eboks.app.domain.interactors.GetFoldersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FoldersComponentPresenter @Inject constructor(val appState: AppStateManager, val getFoldersInteractor: GetFoldersInteractor) :
        FoldersComponentContract.Presenter,
        BasePresenterImpl<FoldersComponentContract.View>(),
        GetFoldersInteractor.Output {


    init {
        refresh()
    }

    override fun refresh() {
        getFoldersInteractor.output = this
        getFoldersInteractor.input = GetFoldersInteractor.Input(false)
        getFoldersInteractor.run()
    }

    override fun setCurrentFolder(folder: Folder) {
        appState.state?.currentFolder = folder
        appState.save()
    }

    override fun onGetFolders(folders: List<Folder>) {
        Timber.e("user folders $folders")
        runAction { v->
            v.showUserFolders(folders)
            v.showRefreshProgress(false)
        }

    }

    override fun onGetSystemFolders(folders: List<Folder>) {
        //Timber.e("system folders $folders")
        runAction { v-> v.showSystemFolders(folders) }
    }

    override fun onGetFoldersError(msg: String) {

    }
}