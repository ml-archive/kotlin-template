package dk.eboks.app.presentation.ui.screens.senders.browse

import dk.eboks.app.domain.interactors.sender.SearchSendersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class BrowseCategoryPresenter(val appStateManager: AppStateManager, val searchSendersInteractor: SearchSendersInteractor) :
        BrowseCategoryContract.Presenter,
        BasePresenterImpl<BrowseCategoryContract.View>(),
        SearchSendersInteractor.Output {

    init {
        searchSendersInteractor.output = this
    }

    override fun loadSenders(senderId: Long) {
        runAction { v ->
            v.showProgress(true)
        }
        val senders = ArrayList<Sender>()

        for(i in 0..60) {
            val r = Math.random()*25 + 65
            val s = Sender(i.toLong(), "${r.toInt().toChar()}senderName$i", 0, "https://qu6oa42ax6a2pyq2c11ozwvm-wpengine.netdna-ssl.com/wp-content/uploads/2017/10/nodes-logo-2017.png")
            senders.add(s)
        }
        runAction { v ->
            v.showProgress(false)
            v.showSenders(senders)
        }
    }

    override fun searchSenders(searchText: String) {
        runAction { v ->
            v.showProgress(true)
        }
        if(searchText.isNotBlank()) {
            searchSendersInteractor.input = SearchSendersInteractor.Input(searchText)
            searchSendersInteractor.run()
        } else {
            onSearchResult(ArrayList()) // empty result
        }
    }

    override fun onSearchResult(senders: List<Sender>) {
        runAction { v ->
            v.showProgress(false)
            v.showSenders(senders)
        }
    }
}