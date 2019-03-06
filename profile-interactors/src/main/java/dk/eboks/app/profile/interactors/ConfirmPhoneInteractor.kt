package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface ConfirmPhoneInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val number: String, val code: String)

    interface Output {
        fun onConfirmPhone()
        fun onConfirmPhoneError(error: ViewError)
    }
}