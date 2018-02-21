package dk.eboks.app.presentation.ui.components.folder.folders

import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FoldersComponentPresenter @Inject constructor(val appState: AppStateManager, val getFoldersInteractor: GetFoldersInteractor, val openFolderInteractor: OpenFolderInteractor) :
        FoldersComponentContract.Presenter,
        BasePresenterImpl<FoldersComponentContract.View>(),
        GetFoldersInteractor.Output,
        OpenFolderInteractor.Output {


    init {
        openFolderInteractor.output = this
        getFoldersInteractor.output = this
        runAction { v-> v.showProgress(true) }
        refresh()
    }

    override fun refresh() {
        getFoldersInteractor.input = GetFoldersInteractor.Input(false)
        getFoldersInteractor.run()
    }

    override fun openFolder(folder: Folder) {
        openFolderInteractor.input = OpenFolderInteractor.Input(folder)
        openFolderInteractor.run()
    }

    override fun onGetFolders(folders: List<Folder>) {
        runAction { v->
            v.showProgress(false)
            v.showUserFolders(folders)
            v.showRefreshProgress(false)
        }

    }

    override fun onGetSystemFolders(folders: List<Folder>) {
        //Timber.e("system folders $folders")
        runAction { v-> v.showSystemFolders(folders) }
    }

    override fun onGetFoldersError(msg: String) {
        Timber.e(msg)
        runAction { v->
            v.showProgress(false)
            v.showRefreshProgress(false)
        }

    }

    override fun onOpenFolderDone() {

    }

    override fun onOpenFolderError(msg: String) {
        Timber.e(msg)
    }
}