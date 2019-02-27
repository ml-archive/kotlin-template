package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteRSAKeyInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onDeleteRSAKeySuccess()

        fun onDeleteRSAKeyError(error: ViewError)
    }
}