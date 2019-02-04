package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/29/2018.
 * @author Christian
 * @since 5/29/2018.
 */
interface ResetPasswordInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val email: String)

    interface Output {
        /** All good! */
        fun onSuccess()

        /** fail */
        fun onError(error: ViewError)
    }
}