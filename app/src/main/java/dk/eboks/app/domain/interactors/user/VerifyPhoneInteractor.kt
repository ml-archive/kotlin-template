package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface VerifyPhoneInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val number: String)

    interface Output {
        fun onVerifyPhone()
        fun onVerifyPhoneError(error: ViewError)
    }
}