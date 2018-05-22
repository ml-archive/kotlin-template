package dk.eboks.app.presentation.ui.components.folder.folders

import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FoldersComponentPresenter @Inject constructor(val appState: AppStateManager, val getFoldersInteractor: GetFoldersInteractor, val openFolderInteractor: OpenFolderInteractor) :
        FoldersComponentContract.Presenter,
        BasePresenterImpl<FoldersComponentContract.View>(),
        GetFoldersInteractor.Output,
        OpenFolderInteractor.Output {

  var pickermode : FolderMode = FolderMode.NORMAL

    init {
        openFolderInteractor.output = this
        getFoldersInteractor.output = this
        runAction { v ->
            v.showProgress(true)
            v.setUser( appState.state?.currentUser)
            pickermode = v.getModeType()
            refresh()
        }

    }

    override fun refresh() {
        getFoldersInteractor.input = GetFoldersInteractor.Input(false, pickermode )
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
        //Timber.e("system folders $folders")
        var proccessedList = processFolders(folders)
        runAction { v -> v.showSystemFolders(proccessedList) }
    }

    private fun processFolders(folders: List<Folder>): List<Folder> {
            // only showing the following folders:
            // "inbox","Archive","Draft","Sent","Deleted"
        val processedList: MutableList<Folder> = mutableListOf<Folder>(Folder(), Folder(), Folder(), Folder(), Folder())
        for (folder in folders) {
            folder.type?.let { type->
                if (type.equals(FolderType.INBOX)) {
                    processedList.removeAt(0)
                    processedList.add(0, folder)
                }
                if (type.equals(FolderType.ARCHIVE)) {
                    processedList.removeAt(1)
                    processedList.add(1, folder)
                }
                if (type.equals(FolderType.DRAFTS)) {
                    processedList.removeAt(2)
                    processedList.add(2, folder)
                }
                if (type.equals(FolderType.SENT)) {
                    processedList.removeAt(3)
                    processedList.add(3, folder)
                }
                if (type.equals(FolderType.DELETED)) {
                    processedList.removeAt(4)
                    processedList.add(4, folder)
                }
            }
        }
        return processedList
    }

    override fun onGetFoldersError(error : ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showRefreshProgress(false)
            v.showErrorDialog(error)
        }

    }

    override fun onOpenFolderDone() {

    }

    override fun onOpenFolderError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}