package dk.eboks.app.presentation.ui.folder.components

import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FoldersComponentPresenter @Inject constructor(
    val appState: AppStateManager,
    val getFoldersInteractor: GetFoldersInteractor,
    val openFolderInteractor: OpenFolderInteractor
) :
    FoldersComponentContract.Presenter,
    BasePresenterImpl<FoldersComponentContract.View>(),
    GetFoldersInteractor.Output,
    OpenFolderInteractor.Output {

    var pickermode: FolderMode = FolderMode.NORMAL

    init {

        openFolderInteractor.output = this
        getFoldersInteractor.output = this
        runAction { v ->
            v.showProgress(true)
            v.setUser(appState.state?.currentUser)
            pickermode = v.getModeType()
            refresh()
        }
    }

    override fun refresh() {
        Timber.d("${view?.isSharedUserActive}")
        val userId =
            if (view?.isSharedUserActive == false) null else appState.state?.impersoniateUser?.userId
        getFoldersInteractor.input = GetFoldersInteractor.Input(false, pickermode, userId)
        getFoldersInteractor.run()
    }

    override fun openFolder(folder: Folder) {
        openFolderInteractor.input = OpenFolderInteractor.Input(folder)
        openFolderInteractor.run()
    }

    override fun onGetFolders(folders: List<Folder>) {
        runAction { v ->
            v.showProgress(false)
            v.showUserFolders(folders)
            v.showRefreshProgress(false)
        }
    }

    override fun onGetSystemFolders(folders: List<Folder>) {
        runAction { v -> v.showSystemFolders(folders) }
    }

    override fun onGetFoldersError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showRefreshProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onOpenFolderDone() {
    }

    override fun onOpenFolderError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}