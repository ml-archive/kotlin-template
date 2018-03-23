package dk.eboks.app.presentation.ui.screens.senders.browse

import dk.eboks.app.domain.interactors.sender.GetSendersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class BrowseCategoryPresenter(val appStateManager: AppStateManager, val getSendersInteractor: GetSendersInteractor) :
        BrowseCategoryContract.Presenter,
        BasePresenterImpl<BrowseCategoryContract.View>(),
        GetSendersInteractor.Output {

    init {
        getSendersInteractor.output = this
    }

    override fun loadSenders(senderId: Long) {
        runAction { v ->
            v.showProgress(true)
        }
        runAction { v ->
            v.showProgress(true)
                getSendersInteractor.input = GetSendersInteractor.Input(false, "", senderId)
                getSendersInteractor.run()
        }
    }

    override fun searchSenders(searchText: String) {
        runAction { v ->
            v.showProgress(true)
            if (searchText.isNotBlank()) {
                getSendersInteractor.input = GetSendersInteractor.Input(false, searchText)
                getSendersInteractor.run()
            } else {
                onGetSenders(ArrayList()) // empty result
            }
        }
    }

    override fun onGetSenders(senders: List<Sender>) {
        runAction { v ->
            v.showProgress(false)
            v.showSenders(senders)
        }
    }

    override fun onGetSendersError(error : ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showSenders(ArrayList()) // empty result
            v.showErrorDialog(error)
        }
    }
}