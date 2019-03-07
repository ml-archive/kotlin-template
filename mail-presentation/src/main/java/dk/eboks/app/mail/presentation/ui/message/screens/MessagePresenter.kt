package dk.eboks.app.mail.presentation.ui.message.screens

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class MessagePresenter @Inject constructor(
    private val appState: AppStateManager,
    private val appConfig: AppConfig,
    private val deleteMessagesInteractor: DeleteMessagesInteractor,
    private val updateMessageInteractor: UpdateMessageInteractor
) : MessageContract.Presenter,
    BasePresenterImpl<MessageContract.View>(),
    DeleteMessagesInteractor.Output,
    UpdateMessageInteractor.Output {
    private var message: Message? = null
    private var moveToFolder: String? = null

    init {
        deleteMessagesInteractor.output = this
        updateMessageInteractor.output = this
    }

    override fun setup() {
        Timber.e("Current message ${appState.state?.currentMessage}")
        message = appState.state?.currentMessage
        view {
            addHeaderComponentFragment()
            addDocumentComponentFragment()
            if (appConfig.isReplyEnabled) {
                message?.reply?.let {
                    addReplyButtonComponentFragment(message!!)
                }
            }
            addNotesComponentFragment()
            if (message?.attachments != null)
                addAttachmentsComponentFragment()
            addShareComponentFragment()
            addFolderInfoComponentFragment()
            message?.let {
                setActionButtons(it)
                showTitle(it)
            }
        }
    }

    override fun moveMessage(folder: Folder) {
        message?.let {
            moveToFolder = folder.name
            updateMessageInteractor.input =
                UpdateMessageInteractor.Input(arrayListOf(it), MessagePatch(folderId = folder.id))
            updateMessageInteractor.run()
        }
    }

    override fun deleteMessage() {
        message?.let {
            deleteMessagesInteractor.input = DeleteMessagesInteractor.Input(arrayListOf(it))
            deleteMessagesInteractor.run()
        }
    }

    override fun onDeleteMessagesSuccess() {
        view { messageDeleted() }
    }

    override fun onDeleteMessagesError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onUpdateMessageSuccess() {
        moveToFolder?.let { name ->
            view {
                updateFolderName(name)
                moveToFolder = null
            }
        }
    }

    override fun onUpdateMessageError(error: ViewError) {
        moveToFolder = null
        view { showErrorDialog(error) }
    }

    override fun archiveMessage() {
        message?.let {
            updateMessageInteractor.input =
                UpdateMessageInteractor.Input(arrayListOf(it), MessagePatch(archive = true))
            updateMessageInteractor.run()
        }
    }

    override fun markAsUnread(unread: Boolean) {
        message?.let {
            updateMessageInteractor.input =
                UpdateMessageInteractor.Input(arrayListOf(it), MessagePatch(unread = unread))
            updateMessageInteractor.run()
        }
    }
}