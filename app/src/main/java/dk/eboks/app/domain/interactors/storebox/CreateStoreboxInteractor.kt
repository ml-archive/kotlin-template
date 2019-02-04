package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/15/2018.
 * @author Christian
 * @since 5/15/2018.
 */
interface CreateStoreboxInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onStoreboxAccountCreated()
        fun onStoreboxAccountExists()
        fun onStoreboxAccountCreatedError(error: ViewError)
    }
}