package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
interface SetCurrentUserInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onSetCurrentUserSuccess()
        fun onSetCurrentUserError(error: ViewError)
    }
}