package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetSendersInteractor : Interactor {
    var output : Output?

    interface Output {
        fun onGetSenders(senders : List<Sender>)
        fun onGetSendersError(msg : String)
    }
}