package dk.eboks.app.keychain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface CreateUserInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val user: User, val password: String)

    interface Output {
        fun onCreateUser(user: User)
        fun onCreateUserError(error: ViewError)
    }
}