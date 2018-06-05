package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.domain.interactor.Interactor

interface GetStoreboxCardLinkInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onGetStoreboxCardLink(result: Link)
        fun onGetStoreboxCardLinkError(error: ViewError)
    }
}