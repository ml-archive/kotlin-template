package dk.eboks.app.domain.interactors.shares

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.nodes.arch.domain.interactor.Interactor

interface GetAllSharesInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onGetAllShares(shares: List<SharedUser>)
        fun onGetAllSharesError(viewError: ViewError)
    }
}