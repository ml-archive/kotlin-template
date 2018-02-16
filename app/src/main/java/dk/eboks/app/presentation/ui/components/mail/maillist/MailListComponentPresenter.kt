package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListComponentPresenter @Inject constructor(val appState: AppStateManager, val getMessagesInteractor : GetMessagesInteractor) :
        MailListComponentContract.Presenter,
        BasePresenterImpl<MailListComponentContract.View>(),
        GetMessagesInteractor.Output {

    init {
        val type = appState.state?.currentFolder?.type ?: FolderType.INBOX
        getMessagesInteractor.output = this
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, 0, type)
        getMessagesInteractor.run()
    }

    override fun refresh() {
        val type = appState.state?.currentFolder?.type ?: FolderType.INBOX
        getMessagesInteractor.input = GetMessagesInteractor.Input(false, 0, type)
        getMessagesInteractor.run()
    }

    override fun setCurrentMessage(message: Message) {
        appState.state?.currentMessage = message
        appState.save()
    }

    override fun onGetMessages(messages: List<Message>) {
        runAction { v->
            v.showRefreshProgress(false)
            v.showMessages(messages)
        }
    }

    override fun onGetMessagesError(msg: String) {
        runAction { v->
            v.showError(msg)
            v.showRefreshProgress(false)
        }
    }
}