package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 3/14/2018.
 * @author   Christian
 * @since    3/14/2018.
 */
interface SearchSendersInteractor : Interactor {
    var output : SearchSendersInteractor.Output?
    var input : SearchSendersInteractor.Input?

    data class Input(val searchText: String)

    interface Output {
        fun onSearchResult(senders : List<Sender>)
    }
}