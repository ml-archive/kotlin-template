package dk.eboks.app.presentation.ui.mail.folder

import dk.eboks.app.domain.interactors.GetFoldersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderPresenter @Inject constructor(val appState: AppStateManager, val getFoldersInteractor: GetFoldersInteractor) :
        FolderContract.Presenter,
        BasePresenterImpl<FolderContract.View>(),
        GetFoldersInteractor.Output
{
    init {
        getFoldersInteractor.output = this
        getFoldersInteractor.input = GetFoldersInteractor.Input(true)
        getFoldersInteractor.run()
    }

    override fun refresh() {
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