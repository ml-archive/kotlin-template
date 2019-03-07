package dk.eboks.app.mail.presentation.ui.folder.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.mail.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.EditFolderInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class NewFolderComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val createFolderInteractor: CreateFolderInteractor,
    private val deleteFolderInteractor: DeleteFolderInteractor,
    private val editFolderInteractor: EditFolderInteractor
) :
    NewFolderComponentContract.Presenter,
    CreateFolderInteractor.Output,
    DeleteFolderInteractor.Output,
    EditFolderInteractor.Output,
    BasePresenterImpl<NewFolderComponentContract.View>() {

    init {
        createFolderInteractor.output = this
        deleteFolderInteractor.output = this
        editFolderInteractor.output = this
        appState.state?.impersoniateUser?.name
            ?: appState.state?.currentUser?.name?.let { user -> view { setRootFolder(user) } }
    }

    override fun createNewFolder(parentFolderId: Int, name: String) {
        val userId =
            if (view?.overrideActiveUser == true) appState.state?.impersoniateUser?.userId else null
        createFolderInteractor.input =
            CreateFolderInteractor.Input(FolderRequest(userId, parentFolderId, name))
        createFolderInteractor.run()
    }

    override fun deleteFolder(folderId: Int) {
        deleteFolderInteractor.input = DeleteFolderInteractor.Input(folderId)
        deleteFolderInteractor.run()
    }

    override fun editFolder(folderId: Int, parentFolderId: Int?, name: String?) {
        editFolderInteractor.input = EditFolderInteractor.Input(folderId, name, parentFolderId)
        editFolderInteractor.run()
    }

    override fun onCreateFolderSuccess() {
        finishView()
    }

    private fun finishView() {
        view {
            finish()
        }
    }

    override fun onCreateFolderError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun folderNameNotAllowed() {
        view { showFolderNameError() }
    }

    override fun onDeleteFolderSuccess() {
        finishView()
    }

    override fun onDeleteFolderError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onEditFolderSuccess() {
        finishView()
    }

    override fun onEditFolderError(error: ViewError) {
        view { showErrorDialog(error) }
    }
}