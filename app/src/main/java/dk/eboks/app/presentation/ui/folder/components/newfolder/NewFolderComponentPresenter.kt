package dk.eboks.app.presentation.ui.folder.components.newfolder

import dk.eboks.app.domain.interactors.folder.UpdateFolderInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.FolderPatch
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NewFolderComponentPresenter @Inject constructor(val appState: AppStateManager,
                                                      val updateFolderInteractor: UpdateFolderInteractor
) :
        NewFolderComponentContract.Presenter,
        UpdateFolderInteractor.Output,
        BasePresenterImpl<NewFolderComponentContract.View>() {

    init {
        updateFolderInteractor.output = this

        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.setRootFolder(user.name)
            }
        }
    }

    override fun saveEditFolder(folderId: Int?, folderPatch: FolderPatch) {
        folderId?.let {
            updateFolderInteractor.input = UpdateFolderInteractor.Input(it, folderPatch)
            updateFolderInteractor.run()
        }
    }

    override fun onUpdateFolderSuccess() {
        runAction { v->
            v.folderUpdated()
        }
    }

    override fun onUpdateFolderError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}