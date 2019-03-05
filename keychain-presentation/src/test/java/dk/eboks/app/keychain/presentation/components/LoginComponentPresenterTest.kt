package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.keychain.interactors.authentication.CheckRSAKeyPresenceInteractor
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test

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

    @Before
    fun setUp() {
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
        presenter.setup()
    }

    @Test
    fun onLoginSuccess() {
    }

    @Test
    fun checkRsaKey() {
    }

    @Test
    fun onLoginDenied() {
    }

    @Test
    fun onLoginError() {
    }

    @Test
    fun fingerPrintConfirmed() {
    }

    @Test
    fun onDecryptSuccess() {
    }

    @Test
    fun onDecryptError() {
    }

    @Test
    fun updateLoginState() {
    }

    @Test
    fun login() {
    }

    @Test
    fun switchLoginProvider() {
    }

    @Test
    fun onLoginActivationCodeRequired() {
    }

    @Test
    fun onCheckRSAKeyPresence() {
    }

    @Test
    fun onCheckRSAKeyPresenceError() {
    }
}