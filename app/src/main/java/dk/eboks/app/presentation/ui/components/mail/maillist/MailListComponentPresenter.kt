package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Message
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

    init {
        openMessageInteractor.output = this
        val type = appState.state?.currentFolder?.type ?: FolderType.INBOX
        getMessagesInteractor.output = this
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, 0, type)
        getMessagesInteractor.run()
        runAction { v-> v.showProgress(true) }
    }

    override fun refresh() {
        val type = appState.state?.currentFolder?.type ?: FolderType.INBOX
        getMessagesInteractor.input = GetMessagesInteractor.Input(false, 0, type)
        getMessagesInteractor.run()
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

    override fun onGetMessagesError(msg: String) {
        runAction { v->
            v.showError(msg)
            v.showProgress(false)
            v.showRefreshProgress(false)
        }
    }

    override fun onOpenMessageDone() {
        runAction { v-> v.showProgress(false) }
    }

    override fun onOpenMessageError(msg: String) {
        runAction { v-> v.showProgress(false) }
        Timber.e(msg)
    }
}