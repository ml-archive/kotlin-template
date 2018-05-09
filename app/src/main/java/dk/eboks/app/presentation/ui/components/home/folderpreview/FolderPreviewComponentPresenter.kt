package dk.eboks.app.presentation.ui.components.home.folderpreview

import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderPreviewComponentPresenter @Inject constructor(val appState: AppStateManager, val getMessagesInteractor: GetMessagesInteractor) :
        FolderPreviewComponentContract.Presenter,
        BasePresenterImpl<FolderPreviewComponentContract.View>(),
        GetMessagesInteractor.Output
{

    override val isVerified = appState.state?.currentUser?.verified ?: false
    var currentFolder: Folder? = null

    init {
        getMessagesInteractor.output = this
    }

    override fun setup(folder: Folder) {
        Timber.e("Got folder $folder")
        currentFolder = folder
        refresh()
    }

    override fun refresh() {
        currentFolder?.let {
            getMessagesInteractor.input = GetMessagesInteractor.Input(cached = true, folder = it)
            getMessagesInteractor.run()
        }
    }

    override fun onGetMessages(messages: List<Message>) {
        runAction { v->
            v.showProgress(false)
            v.showFolder(messages, isVerified)
        }
    }

    override fun onGetMessagesError(error: ViewError) {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}