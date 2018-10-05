package dk.eboks.app.presentation.ui.folder.components.newfolder

import dk.eboks.app.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.domain.interactors.folder.EditFolderInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NewFolderComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val createFolderInteractor: CreateFolderInteractor,
        val deleteFolderInteractor: DeleteFolderInteractor,
        val editFolderInteractor: EditFolderInteractor)
    :
        NewFolderComponentContract.Presenter,
        CreateFolderInteractor.Output,
        DeleteFolderInteractor.Output,
        EditFolderInteractor.Output,
        BasePresenterImpl<NewFolderComponentContract.View>() {

    init {
        createFolderInteractor.output = this
        deleteFolderInteractor.output = this
        editFolderInteractor.output = this
        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.setRootFolder(user.name)
            }
        }
    }

    override fun createNewFolder(parentFolderId: Int, name: String) {
        createFolderInteractor.input = CreateFolderInteractor.Input(FolderRequest(appState.state?.currentUser?.id,parentFolderId,name))
        createFolderInteractor.run()
    }

    override fun deleteFolder(folderId: Int) {
        deleteFolderInteractor.input = DeleteFolderInteractor.Input(folderId)
        deleteFolderInteractor.run()
    }

    override fun editFolder(folderId: Int, parentFolderId: Int?, name: String?) {
        editFolderInteractor.input = EditFolderInteractor.Input(folderId,name,parentFolderId)
        editFolderInteractor.run()
    }

    override fun onCreateFolderSuccess() {
        finishView()
    }

    private fun finishView() {
        runAction { view ->
            view.finish()
        }
    }

    override fun onCreateFolderError(error: ViewError) {
        runAction { view->
            view.showErrorDialog(error)
        }
    }

    override fun folderNameNotAllowed() {
        runAction { view ->
            view.showFolderNameError()
        }
    }

    override fun onDeleteFolderSuccess() {
        finishView()
    }

    override fun onDeleteFolderError(error: ViewError) {
        runAction { view->
            view.showErrorDialog(error)
        }
    }

    override fun onEditFolderSuccess() {
        finishView()
    }

    override fun onEditFolderError(error: ViewError) {
        runAction { view->
            view.showErrorDialog(error)
        }
    }
}