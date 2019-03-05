package dk.eboks.app.profile.presentation.ui.components.drawer

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.profile.interactors.VerifyEmailInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class EmailVerificationComponentPresenterTest {

    private val verifyMailInteractor = mockk<VerifyEmailInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<EmailVerificationComponentContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)
    private lateinit var presenter: EmailVerificationComponentPresenter

    @Before
    fun setUp() {
        presenter = EmailVerificationComponentPresenter(verifyMailInteractor)
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test verify mail`() {
        val email = "test@test.com"
        presenter.verifyMail(email)
        verify {
            viewMock.setVerifyBtnEnabled(false)
            verifyMailInteractor.input = VerifyEmailInteractor.Input(email)
            verifyMailInteractor.run()
        }
    }

    @Test
    fun `Test on verify mail success`() {
        presenter.onVerifyMail()
        verify {
            viewMock.setVerifyBtnEnabled(true)
        }
    }

    @Test
    fun `Test on verify mail error`() {
        val viewError = ViewError()
        presenter.onVerifyMailError(viewError)
        verify {
            viewMock.setVerifyBtnEnabled(true)
            viewMock.showErrorDialog(viewError)
        }

    }
}