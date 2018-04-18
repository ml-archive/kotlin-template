package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListComponentPresenter @Inject constructor(val appState: AppStateManager, val getMessagesInteractor : GetMessagesInteractor, val openMessageInteractor: OpenMessageInteractor) :
        MailListComponentContract.Presenter,
        BasePresenterImpl<MailListComponentContract.View>(),
        GetMessagesInteractor.Output,
        OpenMessageInteractor.Output {


    companion object {
        val FOLDER_MODE = 1
        val SENDER_MODE = 2
    }

    var mode = -1

    var currentFolder : Folder? = null

    init {
        openMessageInteractor.output = this
        getMessagesInteractor.output = this
    }

    override fun setup(folder : Folder) {
        currentFolder = folder
        mode = FOLDER_MODE
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, folder)
        getMessagesInteractor.run()
        runAction { v-> v.showProgress(true) }
    }

    override fun setup(sender : Sender) {
        mode = SENDER_MODE
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, null, sender)
        getMessagesInteractor.run()
        runAction { v-> v.showProgress(true) }
    }

    override fun refresh() {
        when(mode)
        {
            FOLDER_MODE -> {
                currentFolder?.let {
                    getMessagesInteractor.input = GetMessagesInteractor.Input(false, it)
                    getMessagesInteractor.run()
                }
            }
        }

    }

    override fun openMessage(message: Message) {
        runAction { v-> v.showProgress(true) }
        openMessageInteractor.input = OpenMessageInteractor.Input(message)
        openMessageInteractor.run()
    }

    override fun onGetMessages(messages: List<Message>) {
        runAction { v->
            v.showProgress(false)
            v.showRefreshProgress(false)
            if(messages.isNotEmpty()) {
                v.showEmpty(false)
                v.showMessages(messages)
            }
            else
                v.showEmpty(true)
        }
    }

    override fun onGetMessagesError(error : ViewError) {
        runAction { v->
            v.showErrorDialog(error)
            v.showProgress(false)
            v.showRefreshProgress(false)
            v.showEmpty(true)
        }
    }

    override fun onOpenMessageDone() {
        runAction {
            v-> v.showProgress(false)
        }
    }

    override fun onOpenMessageError(error : ViewError) {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}