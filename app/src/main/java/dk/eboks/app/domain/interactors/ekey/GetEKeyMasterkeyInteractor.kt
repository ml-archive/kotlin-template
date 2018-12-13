package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyMasterkeyInteractor: Interactor {
    interface Output {
        fun onGetEKeyMasterkeySuccess(masterKeyResponse: String?, pin: String)
        fun onGetEKeyMasterkeyError(viewError: ViewError)
        fun onGetEkeyMasterkeyNotFound(pin: String)
        fun onAuthError(retryCount: Int)
    }

    data class Input(
            val pin: String,
            val isRetry: Int = 0
    )

    var output: Output?
    var input: Input?
}