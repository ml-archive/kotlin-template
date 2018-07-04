package dk.eboks.app.presentation.ui.uploads.components.myuploads

import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MyUploadsComponentPresenter @Inject constructor(val appState: AppStateManager, val getMessagesInteractor : GetMessagesInteractor, val openMessageInteractor: OpenMessageInteractor) :
        MyUploadsComponentContract.Presenter,
        BasePresenterImpl<MyUploadsComponentContract.View>(),
        GetMessagesInteractor.Output,
        OpenMessageInteractor.Output {

    val folder = appState.state?.currentFolder

    init {
        openMessageInteractor.output = this
        getMessagesInteractor.output = this
        folder?.let {
            getMessagesInteractor.input = GetMessagesInteractor.Input(true, it)
            getMessagesInteractor.run()
            runAction { v-> v.showProgress(true) }
        }.guard {  runAction { v-> v.showEmpty(true) } }

    }

    override fun refresh() {
        folder?.let {
            getMessagesInteractor.input = GetMessagesInteractor.Input(false, it)
            getMessagesInteractor.run()
        }
    }

    override fun openMessage(message: Message) {
        runAction { v-> v.showProgress(true) }
        openMessageInteractor.input = OpenMessageInteractor.Input(message)
        openMessageInteractor.run()
    }

    override fun onOpenMessageDone() {
    }

    override fun onOpenMessageError(error: ViewError) {
    }

    override fun onOpenMessageServerError(serverError: ServerError) {

    }

    override fun onGetMessages(messages: List<Message>) {
    }

    override fun onGetMessagesError(error: ViewError) {

    }

    override fun isViewAttached(): Boolean {
        return view != null
    }
}