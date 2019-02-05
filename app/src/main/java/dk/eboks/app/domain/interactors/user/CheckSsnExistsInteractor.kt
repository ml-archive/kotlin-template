package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface CheckSsnExistsInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val ssn: String)

    interface Output {
        fun onCheckSsnExists(exists: Boolean)
        fun onCheckSsnExists(error: ViewError)
    }
}