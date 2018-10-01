package dk.eboks.app.domain.interactors.authentication.mobileacces

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface ActivateDeviceInteractor: Interactor {
    var input: Input?
    var output: Output?

    data class Input(val key: String)

    interface Output {
        fun onActivateDeviceSuccess()

        fun onActivateDeviceError(error: ViewError, RSAKey : String?)
    }
}