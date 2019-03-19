package dk.eboks.app.profile.presentation.ui.components.drawer

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.ViewController
import dk.eboks.app.profile.interactors.ConfirmPhoneInteractor
import dk.eboks.app.profile.interactors.VerifyPhoneInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PhoneVerifcationComponentPresenterTest {

    private val viewController: ViewController = mockk(relaxUnitFun = true) {
        every { refreshMyInfoComponent } returns true
    }
    private val verifyPhoneInteractor: VerifyPhoneInteractor = mockk(relaxUnitFun = true)
    private val confirmPhoneInteractor: ConfirmPhoneInteractor = mockk(relaxUnitFun = true)

    private val view: PhoneVerificationComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: PhoneVerificationComponentPresenter

    private val mobile = "1241241"

    @Before
    fun setUp() {
        presenter = PhoneVerificationComponentPresenter(viewController, verifyPhoneInteractor, confirmPhoneInteractor)
        presenter.onViewCreated(view, lifecycle)
        presenter.setup(mobile)

        verify { view.showNumber(mobile) }

        assert(presenter.currentMobile == mobile)
    }

    @Test
    fun `Setup Test`() {
        val mobile = "1241241"
        presenter.setup(mobile)
    }

    @Test
    fun `Resend Verification Code Test`() {

        presenter.resendVerificationCode()

        verify {
            view.showProgress(true)
            verifyPhoneInteractor.input = VerifyPhoneInteractor.Input(mobile)
            verifyPhoneInteractor.run()
        }
    }

    @Test
    fun `Confirm Mobile Test`() {

        val code = "code"
        presenter.confirmMobile(code)

        verify {
            confirmPhoneInteractor.input = ConfirmPhoneInteractor.Input(mobile, code)
            confirmPhoneInteractor.run()
        }
    }

    @Test
    fun `On Confirm Phone Test`() {
        presenter.onConfirmPhone()
        verify { view.finishActivity(null) }
    }

    @Test
    fun `On Verify Phone Test`() {
        presenter.onVerifyPhone()
        verify { view.showProgress(false) }
    }

    @Test
    fun `On Error Tests`() {
        val error = ViewError()
        presenter.onConfirmPhoneError(error)
        presenter.onVerifyPhoneError(error)

        verify(exactly = 2) {
            view.showProgress(false)
            view.showErrorDialog(error)
        }
    }
}