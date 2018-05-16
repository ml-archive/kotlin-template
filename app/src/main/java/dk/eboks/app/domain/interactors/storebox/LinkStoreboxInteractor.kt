package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/15/2018.
 * @author   Christian
 * @since    5/15/2018.
 */
interface LinkStoreboxInteractor: Interactor {
    var output : Output?
    var input : Input?

    data class Input(val email : String, val mobile: String)

    interface Output {
        fun storeboxAccountFound(found: Boolean)
        fun onError(error : ViewError)
    }
}