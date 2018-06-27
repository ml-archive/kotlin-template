package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.VerificationState
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/28/2018. (not really, I just copy paste it around a lot :D)
 * @author   Christinus
 * @since    5/28/2018.
 */
interface MergeAndImpersonateInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val verificationState: VerificationState)

    interface Output {
        fun onMergeCompleted()
        fun onMergeError(error: ViewError)
    }
}