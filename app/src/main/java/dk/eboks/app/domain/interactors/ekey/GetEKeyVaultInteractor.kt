package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyVaultInteractor: Interactor {
    data class Input(val signatureTime: String, val signature: String, val pin: String)

    interface Output {
        fun onGetEKeyVaultSuccess(vault: String)
        fun onGetEKeyVaultError(viewError: ViewError)
        fun onGetEKeyVaultNotFound(pin: String)
    }

    var output: Output?
    var input: Input?
}