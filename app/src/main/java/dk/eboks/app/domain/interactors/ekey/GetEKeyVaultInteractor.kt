package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyVaultInteractor: Interactor {
    data class Input(val signatureTime: String, val signature: String)

    interface Output {
        fun onGetEKeyVaultSuccess()
        fun onGetEKeyVaultError(viewError: ViewError)
    }

    var output: Output?
    var input: Input?
}