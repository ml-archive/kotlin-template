package dk.eboks.app.presentation.ui.screens.senders.detail

import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractor
import dk.eboks.app.domain.interactors.sender.GetSendersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 * @author   bison
 * @since    20-05-2017.
 */
class SenderDetailPresenter(val appStateManager: AppStateManager, val getSenderDetailInteractor: GetSenderDetailInteractor) : SenderDetailContract.Presenter, BasePresenterImpl<SenderDetailContract.View>(),
        GetSenderDetailInteractor.Output {

    init {
        getSenderDetailInteractor.output = this
    }

    override fun loadSender(id: Long) {
        getSenderDetailInteractor.input = GetSenderDetailInteractor.Input(id)
        getSenderDetailInteractor.run()
    }

    override fun onGetSender(sender: Sender) {
        runAction { v ->
            v.showSender(sender)
        }
    }

    override fun onGetSenderError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }

}