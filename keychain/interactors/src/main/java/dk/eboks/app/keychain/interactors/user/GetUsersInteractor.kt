package dk.eboks.app.keychain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface GetUsersInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onGetUsers(users: MutableList<User>)
        fun onGetUsersError(error: ViewError)
    }
}