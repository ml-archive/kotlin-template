package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.keychain.interactors.authentication.ResetPasswordInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ForgotPasswordComponentPresenterTest {

    private lateinit var presenter: ForgotPasswordComponentPresenter
    private val resetPasswordInteractor = mockk<ResetPasswordInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<ForgotPasswordComponentContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = ForgotPasswordComponentPresenter(resetPasswordInteractor)
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test reset password`() {
        val email = "test@test.com"
        presenter.resetPassword(email)
        verify {
            resetPasswordInteractor.input = ResetPasswordInteractor.Input(email)
            resetPasswordInteractor.run()
        }
    }

    @Test
    fun `Test on success`() {
        presenter.onSuccess()
        verify { viewMock.showSuccess() }
    }
}