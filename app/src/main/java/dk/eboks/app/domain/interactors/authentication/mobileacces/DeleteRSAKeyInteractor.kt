package dk.eboks.app.domain.interactors.authentication.mobileacces

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteRSAKeyInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onDeleteRSAKeySuccess()

        fun onDeleteRSAKeyError(error: ViewError)
    }
}