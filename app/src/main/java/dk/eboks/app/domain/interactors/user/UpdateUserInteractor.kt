package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.domain.interactor.Interactor

interface UpdateUserInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val user: User)

    interface Output {
        fun onUpdateProfile()
        fun onUpdateProfileError(error : ViewError)
    }
}