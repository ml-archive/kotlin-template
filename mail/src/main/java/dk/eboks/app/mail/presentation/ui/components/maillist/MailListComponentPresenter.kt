package dk.eboks.app.mail.presentation.ui.components.maillist

import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.presentation.ui.components.maillist.MailListComponentContract
import dk.eboks.app.network.util.metaData
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class MailListComponentPresenter @Inject constructor(
        private val getMessagesInteractor: GetMessagesInteractor,
        private val deleteMessagesInteractor: DeleteMessagesInteractor,
        private val moveMessagesInteractor: MoveMessagesInteractor,
        private val updateMessageInteractor: UpdateMessageInteractor
) :
    MailListComponentContract.Presenter,
    BasePresenterImpl<MailListComponentContract.View>(),
    GetMessagesInteractor.Output,
    DeleteMessagesInteractor.Output,
    MoveMessagesInteractor.Output,
    UpdateMessageInteractor.Output {

    companion object {
        private const val FOLDER_MODE = 1
        private const val SENDER_MODE = 2
    }

    var mode = -1

    var currentFolder: Folder? = null
    var currentSender: Sender? = null

    var currentOffset: Int = 0
    private val currentLimit: Int = 30
    var totalMessages: Int = -1

    override var isLoading: Boolean = false

    init {
        getMessagesInteractor.output = this
        deleteMessagesInteractor.output = this
        updateMessageInteractor.output = this
    }

    override fun setup(folder: Folder) {
        currentFolder = folder
        mode = FOLDER_MODE
        isLoading = true
        getMessagesInteractor.input = GetMessagesInteractor.Input(
            cached = false,
            folder = folder,
            offset = currentOffset,
            limit = currentLimit
        )
        getMessagesInteractor.run()
        runAction { v ->
            v.showProgress(true)
        }
    }

    override fun setup(sender: Sender) {
        currentSender = sender
        mode = SENDER_MODE
        isLoading = true
        getMessagesInteractor.input = GetMessagesInteractor.Input(
            cached = false,
            sender = sender,
            offset = currentOffset,
            limit = currentLimit
        )
        getMessagesInteractor.run()
        runAction { v -> v.showProgress(true) }
    }

    override fun loadNextPage() {
        // bail out if were still loading the previous page
        if (isLoading) {
            return
        }
        if (currentOffset + currentLimit < totalMessages - 1) {
            currentOffset += currentLimit
            Timber.e("loading next page.. offset = $currentOffset")
            getMessages()
            runAction { v -> v.showRefreshProgress(true) }
        } else {
            Timber.e("No more pages to load offset = $currentOffset")
        }
    }

    override fun refresh() {
        currentOffset = 0
        getMessages()
    }

    fun getMessages() {
        isLoading = true
        getMessagesInteractor.input = GetMessagesInteractor.Input(
            cached = false,
            folder = currentFolder,
            sender = currentSender,
            offset = currentOffset,
            limit = currentLimit
        )
        getMessagesInteractor.run()
    }

    override fun deleteMessages(selectedMessages: MutableList<Message>) {
        view?.showProgress(true)
        deleteMessagesInteractor.input = DeleteMessagesInteractor.Input(ArrayList(selectedMessages))
        deleteMessagesInteractor.run()
    }

    override fun moveMessages(folderId: Int, selectedMessages: MutableList<Message>) {
        val messageIds = ArrayList(selectedMessages.map { it })
        moveMessagesInteractor.input = MoveMessagesInteractor.Input(folderId, messageIds)
        moveMessagesInteractor.output = this
        moveMessagesInteractor.run()
    }

    override fun markReadMessages(selectedMessages: MutableList<Message>, unread: Boolean) {
        val messagePatch = MessagePatch(unread)
        updateMessage(selectedMessages, messagePatch)
    }

    override fun archiveMessages(selectedMessages: MutableList<Message>) {
        val messagePatch = MessagePatch(archive = true)
        updateMessage(selectedMessages, messagePatch)
    }

    private fun updateMessage(selectedMessages: MutableList<Message>, messagePatch: MessagePatch) {
        view?.showProgress(true)
        val messages = ArrayList(selectedMessages)
        updateMessageInteractor.input = UpdateMessageInteractor.Input(messages, messagePatch)
        updateMessageInteractor.run()
    }

    override fun onGetMessages(messages: List<Message>) {
        isLoading = false
        totalMessages = messages.metaData?.total ?: -1
        Timber.e("Got messages offset = $currentOffset totalMsgs = $totalMessages")
        if (currentOffset == 0) {
            runAction { v ->
                v.showProgress(false)
                v.showRefreshProgress(false)
                if (messages.isNotEmpty()) {
                    v.showEmpty(false)
                    v.showMessages(messages)
                } else
                    v.showEmpty(true)
            }
        } else {
            runAction { v ->
                v.showRefreshProgress(false)
                if (messages.isNotEmpty()) {
                    v.appendMessages(messages)
                }
            }
        }
    }

    override fun onGetMessagesError(error: ViewError) {
        isLoading = false
        runAction { v ->
            v.showErrorDialog(error)
            v.showProgress(false)
            v.showRefreshProgress(false)
            v.showEmpty(true)
        }
    }

    // Delete Messages
    override fun onDeleteMessagesSuccess() {
        Timber.d("onDeleteMessagesSuccess")
        runAction { v ->
            v.showProgress(true)
            refresh()
        }
    }

    override fun onDeleteMessagesError(error: ViewError) {
        Timber.d("onDeleteMessagesError")
        runAction { view ->
            view.showProgress(false)
            view.showErrorDialog(error)
        }
    }

    // Move Messages
    override fun onMoveMessagesSuccess() {
        Timber.d("onMoveMessagesSuccess")
        runAction { v ->
            v.showProgress(true)
            refresh()
        }
    }

    override fun onMoveMessagesError(error: ViewError) {
        Timber.d("onMoveMessagesError")
        runAction { view ->
            view.showProgress(false)
            view.showErrorDialog(error)
        }
    }

    // Update Message
    override fun onUpdateMessageSuccess() {
        Timber.d("onUpdateMessageSuccess")
        runAction { v ->
            v.showProgress(true)
            refresh()
        }
    }

    override fun onUpdateMessageError(error: ViewError) {
        Timber.d("onUpdateMessageError")
        runAction { view ->
            view.showProgress(false)
            view.showErrorDialog(error)
        }
    }
}