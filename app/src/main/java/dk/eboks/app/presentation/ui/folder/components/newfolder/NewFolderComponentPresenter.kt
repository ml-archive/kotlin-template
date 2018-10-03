package dk.eboks.app.presentation.ui.folder.components.newfolder

import dk.eboks.app.domain.interactors.folder.CreateFolderInteractor
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
        val createFolderInteractor: CreateFolderInteractor)
    :
        NewFolderComponentContract.Presenter,
        CreateFolderInteractor.Output,
        BasePresenterImpl<NewFolderComponentContract.View>() {

    init {
        createFolderInteractor.output = this
        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.setRootFolder(user.name)
            }
        }
    }

    override fun createNewFolder(parentFolderId: Int, name: String) {
//        appState.state?.currentUser?.id
        //todo fix this !!
        createFolderInteractor.input = CreateFolderInteractor.Input(FolderRequest(appState.state!!.currentUser!!.id,parentFolderId,name))
        createFolderInteractor.run()
    }

    override fun onCreateFolderSuccess() {
        //todo
        println("succes")
    }

    override fun onCreateFolderError(error: ViewError) {
        //todo
        println("fail")
    }

    override fun folderNameNotAllowed() {
        runAction { view ->
            view.showFolderNameError()
        }
    }
}