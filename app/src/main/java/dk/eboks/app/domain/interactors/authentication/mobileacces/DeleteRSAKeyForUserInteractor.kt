package dk.eboks.app.domain.interactors.authentication.mobileacces

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteRSAKeyForUserInteractor: Interactor {
    var input: Input?
    var output: Output?

    data class Input(val userId: String)

    interface Output {
        fun onDeleteRSAKeyForUserSuccess()
        fun onDeleteRSAKeyForUserError(error: ViewError)
    }
}