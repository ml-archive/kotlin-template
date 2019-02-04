package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface SetEKeyMasterkeyInteractor : Interactor {
    data class Input(
        val masterKey: String,
        val masterKeyHash: String,
        val masterKeyUnencrypted: String
    )

    interface Output {
        fun onSetEKeyMasterkeySuccess(masterKey: String)
        fun onSetEKeyMasterkeyError(viewError: ViewError)
    }

    var input: Input?
    var output: Output?
}