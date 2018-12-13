package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyVaultInteractor: Interactor {
    data class Input(val signatureTime: String, val signature: String, val retryCount: Int)

    interface Output {
        fun onGetEKeyVaultSuccess(vault: String)
        fun onGetEKeyVaultError(viewError: ViewError)
        fun onGetEKeyVaultNotFound()
        fun onAuthError(retryCount: Int)
    }

    var output: Output?
    var input: Input?
}