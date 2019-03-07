package dk.eboks.app.profile.presentation.ui.components.drawer

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.ViewController
import dk.eboks.app.profile.interactors.ConfirmPhoneInteractor
import dk.eboks.app.profile.interactors.VerifyPhoneInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class PhoneVerificationComponentPresenter @Inject constructor(
    private val viewController: ViewController,
    private val verifyPhoneInteractor: VerifyPhoneInteractor,
    private val confirmPhoneInteractor: ConfirmPhoneInteractor
) :
    PhoneVerificationComponentContract.Presenter,
    BasePresenterImpl<PhoneVerificationComponentContract.View>(),
    VerifyPhoneInteractor.Output,
    ConfirmPhoneInteractor.Output {

    init {
        verifyPhoneInteractor.output = this
        confirmPhoneInteractor.output = this
    }

    private var currentMobile: String? = null

    override fun setup(mobile: String) {
        currentMobile = mobile
        view { showNumber(mobile) }
        // verifyPhoneInteractor.input = VerifyPhoneInteractor.Input(mobile)
        // verifyPhoneInteractor.run()
    }

    override fun resendVerificationCode() {
        view { showProgress(true) }
        currentMobile?.let {
            verifyPhoneInteractor.input = VerifyPhoneInteractor.Input(it)
            verifyPhoneInteractor.run()
        }
    }

    override fun confirmMobile(activationCode: String) {
        currentMobile?.let { mobile ->
            confirmPhoneInteractor.input = ConfirmPhoneInteractor.Input(mobile, activationCode)
            confirmPhoneInteractor.run()
        }
    }

    /**
     * VerifyPhoneInteractor callbacks
     */

    override fun onVerifyPhone() {
        viewController.refreshMyInfoComponent = true
        view { showProgress(false) }
    }

    override fun onVerifyPhoneError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    /**
     * ConfirmPhoneInteractor callbacks
     */
    override fun onConfirmPhone() {
        viewController.refreshMyInfoComponent = true
        view { finishActivity(null) }
    }

    override fun onConfirmPhoneError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }
}