package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.protocol.UserInfo
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface LoginInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val username : String, val password: String, val activationCode: String?)

    interface Output {
        /** All good! */
        fun onLoginSuccess(response: AccessToken)
        /** Auth-fail */
        fun onLoginDenied(error : ViewError)
        /** Network-fail */
        fun onLoginError(error : ViewError)
    }
}