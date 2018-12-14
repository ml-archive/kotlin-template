package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyMasterkeyInteractor: Interactor {
    interface Output {
        fun onGetEKeyMasterkeySuccess(masterKeyResponse: String?)
        fun onGetEKeyMasterkeyError(viewError: ViewError)
        fun onGetEkeyMasterkeyNotFound()
        fun onAuthError(retryCount: Int)
    }

    data class Input(
            val isRetry: Int = 0
    )

    var output: Output?
    var input: Input?
}