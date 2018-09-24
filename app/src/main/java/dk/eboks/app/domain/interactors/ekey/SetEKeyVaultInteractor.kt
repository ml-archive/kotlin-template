package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface SetEKeyVaultInteractor : Interactor {
    data class Input(var vault: String, val signatureTime: String, val signature: String)

    interface Output {
        fun onSetEKeyVaultSuccess()
        fun onSetEKeyVaultError(viewError: ViewError)
    }

    var input: Input?
    var output: Output?
}