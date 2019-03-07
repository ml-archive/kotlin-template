package dk.eboks.app.presentation.ui.home.components.folderpreview

import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderPreviewComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getMessagesInteractor: GetMessagesInteractor
) :
    FolderPreviewComponentContract.Presenter,
    BasePresenterImpl<FolderPreviewComponentContract.View>(),
    GetMessagesInteractor.Output {

    // override val isVerified = appState.state?.currentUser?.verified ?: false
    private var currentFolder: Folder? = null

    init {
        getMessagesInteractor.output = this
    }

    override fun setup(folder: Folder) {
        Timber.e("Got folder $folder")
        currentFolder = folder
        refresh(true)
    }

    override fun refresh(cached: Boolean) {
        currentFolder?.let {
            getMessagesInteractor.input = GetMessagesInteractor.Input(cached = cached, folder = it)
            getMessagesInteractor.run()
        }
    }

    override fun onGetMessages(messages: List<Message>) {
        view {
            EventBus.getDefault().post(RefreshFolderPreviewDoneEvent())
            showProgress(false)
            showFolder(messages, appState.state?.currentUser?.verified ?: false)
        }
    }

    override fun onGetMessagesError(error: ViewError) {
        view {
            EventBus.getDefault().post(RefreshFolderPreviewDoneEvent())
            showProgress(false)
            showErrorDialog(error)
        }
    }
}