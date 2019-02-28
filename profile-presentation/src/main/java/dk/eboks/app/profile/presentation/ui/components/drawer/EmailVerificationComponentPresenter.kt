package dk.eboks.app.profile.presentation.ui.components.drawer

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.profile.interactors.VerifyEmailInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EmailVerificationComponentPresenter @Inject constructor(
    private val verifyMailInteractor: VerifyEmailInteractor
) :
    EmailVerificationComponentContract.Presenter,
    BasePresenterImpl<EmailVerificationComponentContract.View>(),
    VerifyEmailInteractor.Output {

    init {
        verifyMailInteractor.output = this
    }

    override fun verifyMail(mail: String) {
        runAction { v ->
            v.setVerifyBtnEnabled(false)
        }
        verifyMailInteractor.input = VerifyEmailInteractor.Input(mail)
        verifyMailInteractor.run()
    }

    override fun onVerifyMail() {
        runAction { v ->
            v.setVerifyBtnEnabled(true)
        }
    }

    override fun onVerifyMailError(error: ViewError) {
        runAction { v ->
            v.setVerifyBtnEnabled(true)
            v.showErrorDialog(error)
        }
    }
}