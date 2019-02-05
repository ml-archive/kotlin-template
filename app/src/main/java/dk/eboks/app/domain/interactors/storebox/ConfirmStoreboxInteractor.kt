package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/15/2018.
 * @author Christian
 * @since 5/15/2018.
 */
interface ConfirmStoreboxInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val id: String, val code: String)

    interface Output {
        fun onLinkingSuccess(result: Boolean)
        fun onError(error: ViewError)
    }
}