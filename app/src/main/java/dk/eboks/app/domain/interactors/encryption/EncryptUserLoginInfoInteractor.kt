package dk.eboks.app.domain.interactors.encryption

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.nodes.arch.domain.interactor.Interactor

interface EncryptUserLoginInfoInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val loginInfo: LoginInfo)
    interface Output {
        fun onSuccess()
        fun onError(e: ViewError)
    }
}