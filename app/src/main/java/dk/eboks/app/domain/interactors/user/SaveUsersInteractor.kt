package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface SaveUsersInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onSaveUsers()
        fun onSaveUsersError(error: ViewError)
    }
}