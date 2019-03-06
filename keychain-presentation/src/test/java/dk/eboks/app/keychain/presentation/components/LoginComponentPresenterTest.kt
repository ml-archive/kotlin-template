package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.keychain.interactors.authentication.CheckRSAKeyPresenceInteractor
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class LoginComponentPresenterTest {

    private val appState = mockk<AppStateManager>(relaxUnitFun = true)
    private val userSettingsManager = mockk<UserSettingsManager>(relaxUnitFun = true)
    private val decryptUserLoginInfoInteractor =
        mockk<DecryptUserLoginInfoInteractor>(relaxUnitFun = true)
    private val loginInteractor = mockk<LoginInteractor>(relaxUnitFun = true)
    private val checkRSAKeyPresenceInteractor =
        mockk<CheckRSAKeyPresenceInteractor>(relaxUnitFun = true)
    private val appConfig = mockk<AppConfig>(relaxUnitFun = true)
    private val viewMock = mockk<LoginComponentContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)
    private lateinit var presenter: LoginComponentPresenter
    private val mockUserId = Random.nextInt()
    private val mockUser = User(id = mockUserId)

    @Before
    fun setUp() {
        every { appConfig.alternativeLoginProviders } returns listOf()
        every { appState.state } returns AppState()
        every { appState.state?.currentUser } returns mockUser
        presenter = LoginComponentPresenter(
            appState,
            userSettingsManager,
            decryptUserLoginInfoInteractor,
            loginInteractor,
            checkRSAKeyPresenceInteractor,
            appConfig
        )
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test setup`() {
        // TODO
    }

    @Test
    fun `Test check RSA Key`() {
        presenter.checkRsaKey()
        verify {
            checkRSAKeyPresenceInteractor.input =
                CheckRSAKeyPresenceInteractor.Input(mockUserId.toString())
            checkRSAKeyPresenceInteractor.run()
        }
    }

    @Test
    fun `Test on login denied`() {
        val viewError = ViewError()
        presenter.onLoginDenied(viewError)
        verify {
            viewMock.showProgress(false)
            viewMock.showError(viewError)
        }
    }

    @Test
    fun `Test on login error`() {
        val viewError = ViewError()
        presenter.onLoginError(viewError)
        verify {
            viewMock.showProgress(false)
            viewMock.showError(viewError)
        }
    }

    @Test
    fun `Test finger print confirmed`() {
        presenter.fingerPrintConfirmed(mockUser)
        verify {
            viewMock.showProgress(true)
            decryptUserLoginInfoInteractor.run()
        }
    }

    @Test
    fun `Test decrypt success`() {
        // TODO
    }

    @Test
    fun `Test on decrypt error`() {
        val error = ViewError()
        presenter.onDecryptError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showError(error)
        }
    }

    @Test
    fun updateLoginState() {
    }

    @Test
    fun `Test login`() {
        val loginState = LoginState()
        every { appState.state?.loginState } returns loginState
        presenter.login()
        verify {
            viewMock.showProgress(true)
            loginInteractor.input = LoginInteractor.Input(loginState)
            loginInteractor.run()
        }
    }

    @Test
    fun `Test switch login provider`() {

    }

    @Test
    fun `Test on login activation code required`() {
        presenter.onLoginA
    }

    @Test
    fun onCheckRSAKeyPresence() {
    }

    @Test
    fun onCheckRSAKeyPresenceError() {
    }
}