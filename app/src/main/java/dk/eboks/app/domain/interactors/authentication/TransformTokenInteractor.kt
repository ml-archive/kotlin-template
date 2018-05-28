package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.LoginState
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
interface TransformTokenInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val loginState: LoginState)

    interface Output {
        /** All good! */
        fun onLoginSuccess(response: AccessToken)

        /** Auth-fail */
        fun onLoginError(error: ViewError)
    }
}