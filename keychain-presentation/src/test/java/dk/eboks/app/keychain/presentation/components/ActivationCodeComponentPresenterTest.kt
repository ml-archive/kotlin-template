package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ActivationCodeComponentPresenterTest {

    private lateinit var presenter: ActivationCodeComponentPresenter
    private val loginInteractor = mockk<LoginInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<ActivationCodeComponentContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)
    private val appConfig = mockk<AppConfig>(relaxUnitFun = true)
    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val loginState = LoginState(activationCode = "1234")

    @Before
    fun setUp() {
        every { appConfig.isDebug } returns false
        every { appStateManager.state } returns AppState(loginState = loginState)
        presenter = ActivationCodeComponentPresenter(appConfig, appStateManager, loginInteractor)
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test login`() {
        presenter.login()
        verify {
            viewMock.showProgress(true)
            loginInteractor.input = LoginInteractor.Input(loginState)
            loginInteractor.run()
        }
    }

    @Test
    fun `Test login success`() {
        presenter.onLoginSuccess(AccessToken("", 0, "", ""))
        verify { viewMock.proceedToApp() }
    }

    @Test
    fun `Test login activation code required `() {
        presenter.onLoginActivationCodeRequired()
        verify { viewMock.showError(any()) }
    }

    @Test
    fun `Test login denied`() {
        val error = ViewError()
        presenter.onLoginDenied(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test login error`() {
        val error = ViewError()
        presenter.onLoginError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }
}