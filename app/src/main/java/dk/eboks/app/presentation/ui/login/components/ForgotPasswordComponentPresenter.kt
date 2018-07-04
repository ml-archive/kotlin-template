package dk.eboks.app.presentation.ui.login.components

import dk.eboks.app.domain.interactors.authentication.ResetPasswordInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ForgotPasswordComponentPresenter @Inject constructor(
        val resetPasswordInteractor: ResetPasswordInteractor
) :
        ForgotPasswordComponentContract.Presenter,
        BasePresenterImpl<ForgotPasswordComponentContract.View>(),
        ResetPasswordInteractor.Output {

    init {
        resetPasswordInteractor.output = this
    }

    override fun resetPassword(email: String) {
        resetPasswordInteractor.input = ResetPasswordInteractor.Input(email)
        resetPasswordInteractor.run()
    }

    override fun onSuccess() {
        runAction { v ->
            v.showSuccess()
        }
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showError(error)
        }
    }


}