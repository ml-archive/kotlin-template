package dk.eboks.app.mail.presentation.ui.message.screens

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
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
        runAction { v ->
            v.addHeaderComponentFragment()

            // TODO Remove mocked payment info
            if (appConfig.isPaymentEnabled) {
            }

            v.addDocumentComponentFragment()
            if (appConfig.isReplyEnabled) {
                message?.reply?.let {
                    v.addReplyButtonComponentFragment(message!!)
                }
            }
            v.addNotesComponentFragment()
            if (message?.attachments != null)
                v.addAttachmentsComponentFragment()
            v.addShareComponentFragment()
            v.addFolderInfoComponentFragment()
            message?.let {
                v.setActionButtons(it)
                v.showTitle(it)
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
        runAction { it.messageDeleted() }
    }

    override fun onDeleteMessagesError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onUpdateMessageSuccess() {
        moveToFolder?.let { name ->
            runAction {
                it.updateFolderName(name)
                moveToFolder = null
            }
        }
    }

    override fun onUpdateMessageError(error: ViewError) {
        moveToFolder = null
        runAction { view ->
            view.showErrorDialog(error)
        }
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