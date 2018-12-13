package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface SetEKeyVaultInteractor : Interactor {
    data class Input(var vault: String, val signatureTime: String, val signature: String, val retryCount: Int)

    interface Output {
        fun onSetEKeyVaultSuccess()
        fun onSetEKeyVaultError(viewError: ViewError)
        fun onAuthError(retryCount: Int)
    }

    var input: Input?
    var output: Output?
}