package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/7/2018.
 * @author   Christian
 * @since    5/7/2018.
 */
interface GetUserProfileInteractor : Interactor {

    var output : GetUserProfileInteractor.Output?

    interface Output {
        fun onGetUser(user: User)
        fun onGetUserError(error: ViewError)
    }
}