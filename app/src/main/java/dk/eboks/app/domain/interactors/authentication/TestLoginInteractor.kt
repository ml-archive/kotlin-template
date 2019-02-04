package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface TestLoginInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val username: String, val password: String, val activationCode: String? = null)

    interface Output {
        /** All good! */
        fun onTestLoginSuccess()

        /** Auth-fail */
        fun onTestLoginDenied(error: ViewError)

        /** Network-fail */
        fun onTestLoginError(error: ViewError)
    }
}