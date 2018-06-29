package dk.eboks.app.presentation.ui.components.profile.drawer

import dk.eboks.app.domain.interactors.user.UpdateUserInteractor
import dk.eboks.app.domain.interactors.user.VerifyEmailInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EmailVerificationComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val verifyMailInteractor: VerifyEmailInteractor
) :
        EmailVerificationComponentContract.Presenter,
        BasePresenterImpl<EmailVerificationComponentContract.View>(),
        VerifyEmailInteractor.Output {

    init {
        verifyMailInteractor.output = this
    }

    override fun verifyMail(mail: String) {
        runAction { v ->
            verifyMailInteractor.input = VerifyEmailInteractor.Input(mail)
            verifyMailInteractor.run()
        }
    }

    override fun onVerifyMail() {
        runAction { v->v.finishActivity(null) }
    }

    override fun onVerifyMailError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }
}


