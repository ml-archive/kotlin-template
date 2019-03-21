package dk.eboks.app.profile.interactors

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