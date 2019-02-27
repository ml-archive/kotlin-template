package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.LoginState
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface LoginInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val loginState: LoginState, val bearerToken: String? = null)

    interface Output {
        /** All good! */
        fun onLoginSuccess(response: AccessToken)

        /** First-time failure */
        fun onLoginActivationCodeRequired()

        /** Auth-fail */
        fun onLoginDenied(error: ViewError)

        /** Network-fail */
        fun onLoginError(error: ViewError)
    }
}