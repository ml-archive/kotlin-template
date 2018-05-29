package dk.eboks.app.domain.interactors.signup

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface CheckSignupMailInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val email: String)

    interface Output {
        fun onVerifySignupMail(exists : Boolean)
        fun onVerifySignupMail(error : ViewError)
    }
}