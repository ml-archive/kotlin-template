package dk.eboks.app.presentation.ui.mail.list

import dk.eboks.app.domain.interactors.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListPresenter @Inject constructor(val appState: AppStateManager, val getMessagesInteractor: GetMessagesInteractor) :
        MailListContract.Presenter,
        BasePresenterImpl<MailListContract.View>(),
        GetMessagesInteractor.Output
{
    var refreshing = false

    init {
        getMessagesInteractor.output = this
        getMessagesInteractor.input = GetMessagesInteractor.Input(true, 0)
        getMessagesInteractor.run()
    }

    override fun refresh() {
        getMessagesInteractor.input = GetMessagesInteractor.Input(false, 0)
        getMessagesInteractor.run()
    }

    override fun setCurrentMessage(message: Message) {
        appState.state?.currentMessage = message
        appState.save()
    }

    override fun onGetMessages(messages: List<Message>) {
        runAction { v-> v.showMessages(messages) }
    }

    override fun onGetMessagesError(msg: String) {
        runAction { v-> v.showError(msg) }
    }
}