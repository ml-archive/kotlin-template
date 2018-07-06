package dk.eboks.app.presentation.ui.mail.components.maillist

import dk.eboks.app.domain.interactors.message.DeleteMessagesInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.MoveMessagesInteractor
import dk.eboks.app.domain.interactors.message.UpdateMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.network.util.metaData
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class MailListComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val getMessagesInteractor: GetMessagesInteractor,
        val deleteMessagesInteractor: DeleteMessagesInteractor,
        val moveMessagesInteractor: MoveMessagesInteractor,
        val updateMessageInteractor: UpdateMessageInteractor
) :
        MailListComponentContract.Presenter,
        BasePresenterImpl<MailListComponentContract.View>(),
        GetMessagesInteractor.Output, DeleteMessagesInteractor.Output,
        MoveMessagesInteractor.Output, UpdateMessageInteractor.Output
{

    companion object {
        val FOLDER_MODE = 1
        val SENDER_MODE = 2
    }

    var mode = -1

    var currentFolder: Folder? = null
    var currentSender: Sender? = null

    var currentOffset : Int = 0
    var currentLimit : Int = 20
    var totalMessages : Int = -1

    override var isLoading : Boolean = false

    init {
        getMessagesInteractor.output = this
    }

    override fun setup(folder: Folder) {
        currentFolder = folder
        mode = FOLDER_MODE
        isLoading = true
        getMessagesInteractor.input = GetMessagesInteractor.Input(cached = false, folder = folder, offset = currentOffset, limit = currentLimit)
        getMessagesInteractor.run()
        runAction { v ->
            v.showProgress(true)
        }
    }

    override fun setup(sender: Sender) {
        currentSender = sender
        mode = SENDER_MODE
        isLoading = true
        getMessagesInteractor.input = GetMessagesInteractor.Input(cached = false, sender = sender, offset = currentOffset, limit = currentLimit)
        getMessagesInteractor.run()
        runAction { v -> v.showProgress(true) }
    }

    override fun loadNextPage() {
        // bail out if were still loading the previous page
        if(isLoading)
            return
        if(currentOffset + currentLimit < totalMessages-1) {
            currentOffset += currentLimit
            Timber.e("loading next page.. offset = $currentOffset")
            isLoading = true
            getMessagesInteractor.input = GetMessagesInteractor.Input(
                    cached = false,
                    folder = currentFolder,
                    sender = currentSender,
                    offset = currentOffset,
                    limit = currentLimit)
            getMessagesInteractor.run()
            runAction { v->v.showRefreshProgress(true) }
        }
        else
        {
            Timber.e("No more pages to load offset = $currentOffset")
        }
    }

    override fun refresh() {
        currentOffset = 0
        isLoading = true
        getMessagesInteractor.input = GetMessagesInteractor.Input(cached = false, folder = currentFolder, sender = currentSender, offset = currentOffset, limit = currentLimit)
        getMessagesInteractor.run()

        /*
        when (mode) {
            FOLDER_MODE -> {
                currentFolder?.let {
                    getMessagesInteractor.input = GetMessagesInteractor.Input(false, it)
                    getMessagesInteractor.run()
                }
            }
            SENDER_MODE -> {
                currentSender?.let {
                    getMessagesInteractor.input = GetMessagesInteractor.Input(true, null, it)
                    getMessagesInteractor.run()
                }
            }
        }
        */
    }

    override fun updateMessage(message: Message) {
        view?.showProgress(true)

        // TODO do something to the stuff below
        /*
        updateMessageInteractor.input = UpdateMessageInteractor.Input(message)
        updateMessageInteractor.output = this
        updateMessageInteractor.run()
        */
    }

    override fun deleteMessages(selectedMessages: MutableList<Message>) {
        view?.showProgress(true)

        val messageIds = ArrayList(selectedMessages.map { it.id })

        deleteMessagesInteractor.input = DeleteMessagesInteractor.Input(messageIds)
        deleteMessagesInteractor.output = this
        deleteMessagesInteractor.run()
    }

    override fun moveMessages(folderName: String?, selectedMessages: MutableList<Message>) {
        if (folderName == null) {
            return
        }

        val messageIds = ArrayList(selectedMessages.map { it.id })

        moveMessagesInteractor.input = MoveMessagesInteractor.Input(folderName, messageIds)
        moveMessagesInteractor.output = this

        moveMessagesInteractor.run()
    }

    override fun markReadMessages(selectedMessages: MutableList<Message>) {

    }

    override fun markUnreadMessages(selectedMessages: MutableList<Message>) {

    }

    override fun archiveMessages(selectedMessages: MutableList<Message>) {

    }

    override fun onGetMessages(messages: List<Message>) {
        isLoading = false
        totalMessages = messages.metaData?.total ?: -1
        Timber.e("Got messages offset = $currentOffset totalMsgs = $totalMessages")
        if(currentOffset == 0) {
            runAction { v ->
                v.showProgress(false)
                v.showRefreshProgress(false)
                if (messages.isNotEmpty()) {
                    v.showEmpty(false)
                    v.showMessages(messages)
                } else
                    v.showEmpty(true)
            }
        }
        else
        {
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