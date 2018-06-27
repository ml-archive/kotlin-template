package dk.eboks.app.domain.interactors.authentication

import android.os.Parcel
import android.os.Parcelable
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.VerificationState
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
interface VerifyProfileInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val verificationState: VerificationState)

    interface Output {
        fun onVerificationSuccess()
        fun onAlreadyVerifiedProfile()
        fun onVerificationError(error: ViewError)
    }
}