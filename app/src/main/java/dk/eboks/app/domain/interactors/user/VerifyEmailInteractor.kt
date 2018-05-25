package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface VerifyEmailInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val mail: String)

    interface Output {
        fun onVerifyMail()
        fun onVerifyMailError(error : ViewError)
    }
}