package dk.eboks.app.presentation.ui.mail

import dk.eboks.app.domain.interactors.GetMessagesInteractor
import dk.eboks.app.domain.models.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailFolderPresenter @Inject constructor(val getMessagesInteractor: GetMessagesInteractor) :
        MailFolderContract.Presenter,
        BasePresenterImpl<MailFolderContract.View>(),
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

    override fun onGetMessages(messages: List<Message>) {
        runAction { v-> v.showMessages(messages) }
    }

    override fun onGetMessagesError(msg: String) {
        runAction { v-> v.showError(msg) }
    }
}