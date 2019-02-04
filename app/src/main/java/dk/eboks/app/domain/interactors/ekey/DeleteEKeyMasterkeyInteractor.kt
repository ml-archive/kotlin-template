package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteEKeyMasterkeyInteractor : Interactor {
    interface Output {
        fun onDeleteEKeyMasterkeySuccess()
        fun onDeleteEKeyMasterkeyError(viewError: ViewError)
    }

    var output: Output?
}