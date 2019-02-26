package dk.eboks.app.keychain.interactors.encryption

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 6/22/2018.
 * @author Christian
 * @since 6/22/2018.
 */
interface DecryptUserLoginInfoInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onDecryptSuccess(loginInfo: LoginInfo)
        fun onDecryptError(error: ViewError)
    }
}