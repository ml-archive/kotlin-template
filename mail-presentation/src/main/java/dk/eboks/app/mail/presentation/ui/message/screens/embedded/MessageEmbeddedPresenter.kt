package dk.eboks.app.mail.presentation.ui.message.screens.embedded

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class MessageEmbeddedPresenter @Inject constructor(
    private val appConfig: AppConfig,
    private val stateManager: AppStateManager,
    private val deleteMessagesInteractor: DeleteMessagesInteractor,
    private val updateMessageInteractor: UpdateMessageInteractor
) :
    MessageEmbeddedContract.Presenter,
    BasePresenterImpl<MessageEmbeddedContract.View>(),
    DeleteMessagesInteractor.Output,
    UpdateMessageInteractor.Output {

    init {
        deleteMessagesInteractor.output = this
        updateMessageInteractor.output = this
    }

    var message: Message? = null
    private var moveToFolder: String? = null

    override fun setup() {
        message = stateManager.state?.currentMessage
        setupDrawerHeader()
        startViewer()
        view {
            addHeaderComponentFragment()
            message?.let { message ->
                if (appConfig.isReplyEnabled) {
                    message.reply?.let {
                        addReplyButtonComponentFragment(message)
                    }
                }
                if (appConfig.isSignEnabled) {
                    message.sign?.let {
                        addSignButtonComponentFragment(message)
                    }
                }
                if (appConfig.isDocumentActionsEnabled) {
                    setActionButton(message)
                }

                addNotesComponentFragment()
                if (message.attachments != null)
                    addAttachmentsComponentFragment()
                addShareComponentFragment()
                addFolderInfoComponentFragment()
                showTitle(message)
            }
        }
    }

    private fun setupDrawerHeader() {
        if (message?.numberOfAttachments ?: 0 > 0)
            view { setHighPeakHeight() }
    }

    private fun startViewer() {
        message?.content?.mimeType?.let { mimetype ->
            if (mimetype.startsWith("image/", true)) {
                view { addImageViewer() }
                return
            }
            if (mimetype == "application/pdf") {
                view { addPdfViewer() }
                return
            }
            if (mimetype == "text/html") {
                view { addHtmlViewer() }
                return
            }
            if (mimetype.startsWith("text/", true)) {
                view { addTextViewer() }
                return
            }
        }
    }

    private fun updateMessage(messages: ArrayList<Message>, messagePatch: MessagePatch) {
        message?.let {
            updateMessageInteractor.input = UpdateMessageInteractor.Input(messages, messagePatch)
            updateMessageInteractor.run()
        }
    }

    override fun moveMessage(folder: Folder) {
        message?.let {
            moveToFolder = folder.name
            updateMessage(arrayListOf(it), MessagePatch(folderId = folder.id))
        }
    }

    override fun deleteMessage() {
        message?.let {
            deleteMessagesInteractor.input = DeleteMessagesInteractor.Input(arrayListOf(it))
            deleteMessagesInteractor.run()
        }
    }

    override fun archiveMessage() {
        message?.let {
            updateMessage(arrayListOf(it), MessagePatch(null, true, null, null))
        }
    }

    override fun markMessageRead() {
        message?.let {
            updateMessage(arrayListOf(it), MessagePatch(false, null, null, null))
        }
    }

    override fun markMessageUnread() {
        message?.let {
            updateMessage(arrayListOf(it), MessagePatch(true, null, null, null))
        }
    }

    override fun onDeleteMessagesSuccess() {
        view { messageDeleted() }
    }

    override fun onDeleteMessagesError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onUpdateMessageSuccess() {
        moveToFolder?.let { view { updateFolderName(it) } }
    }

    override fun onUpdateMessageError(error: ViewError) {
        moveToFolder = null
        view { showErrorDialog(error) }
    }
}