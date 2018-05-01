package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.interactors.message.DeleteMessagesInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.MoveMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.ui.screens.overlay.ButtonType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class MailListComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val getMessagesInteractor: GetMessagesInteractor,
        val deleteMessagesInteractor: DeleteMessagesInteractor,
        val moveMessagesInteractor: MoveMessagesInteractor,
        val openMessageInteractor: OpenMessageInteractor
) :
        MailListComponentContract.Presenter,
        BasePresenterImpl<MailListComponentContract.View>(),
        GetMessagesInteractor.Output, DeleteMessagesInteractor.Output,
        MoveMessagesInteractor.Output, OpenMessageInteractor.Output {

    companion object {
        val FOLDER_MODE = 1
        val SENDER_MODE = 2
    }

    var mode = -1

    var currentFolder: Folder? = null

    init {
        getMessagesInteractor.output = this
    }

    override fun setup(folder: Folder) {
        currentFolder = folder
        mode = FOLDER_MODE
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, folder)
        getMessagesInteractor.run()
        runAction { v ->
            v.showProgress(true)
        }
    }

    override fun setup(sender: Sender) {
        mode = SENDER_MODE
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, null, sender)
        getMessagesInteractor.run()
        runAction { v -> v.showProgress(true) }
    }

    override fun refresh() {
        when (mode) {
            FOLDER_MODE -> {
                currentFolder?.let {
                    getMessagesInteractor.input = GetMessagesInteractor.Input(false, it)
                    getMessagesInteractor.run()
                }
            }
        }
    }

    override fun openMessage(message: Message, type: ButtonType) {
        view?.showProgress(true)

        // TODO stinus there are 3 types of actions that can occur refer to the code that calls this

        openMessageInteractor.input = OpenMessageInteractor.Input(message)
        openMessageInteractor.output = this

        openMessageInteractor.run()
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

    override fun onGetMessages(messages: List<Message>) {
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

    override fun onGetMessagesError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
            v.showProgress(false)
            v.showRefreshProgress(false)
            v.showEmpty(true)
        }
    }
    // Open Message

    override fun onOpenMessageDone() {
        // TODO @STPE FIX THIS
        runAction { view ->
            view.showProgress(false)
        }
    }

    override fun onOpenMessageServerError(serverError: ServerError) {
        // TODO HANDLE ERROR
        runAction { view ->
            view.showProgress(false)
            // view.showErrorDialog(serverError)
        }
    }

    override fun onOpenMessageError(error: ViewError) {
        Timber.d("onOpenMessageError")
        runAction { view ->
            view.showProgress(false)
            view.showErrorDialog(error)
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
}