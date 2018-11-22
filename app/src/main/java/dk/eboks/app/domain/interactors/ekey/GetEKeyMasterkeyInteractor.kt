package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyMasterkeyInteractor: Interactor {
    interface Output {
        fun onGetEKeyMasterkeySuccess(masterKeyResponse: String?, pin: String)
        fun onGetEKeyMasterkeyError(viewError: ViewError)
        fun onGetEkeyMasterkeyNotFound(pin: String)
    }

    data class Input(
            val pin: String
    )

    var output: Output?
    var input: Input?
}