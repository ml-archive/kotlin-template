package dk.eboks.app.profile.presentation.ui.components.drawer

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.TestLoginInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.profile.interactors.SaveUserInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FingerPrintComponentPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val userSettingsManager: UserSettingsManager = mockk(relaxUnitFun = true)
    private val encryptUserLoginInfoInteractor: EncryptUserLoginInfoInteractor = mockk(relaxUnitFun = true)
    private val saveUserLoginInfoInteractor: SaveUserInteractor = mockk(relaxUnitFun = true)
    private val testLoginInteractor: TestLoginInteractor = mockk(relaxUnitFun = true)

    private val view: FingerPrintComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: FingerPrintComponentPresenter

    @Before
    fun setUp() {
        presenter = FingerPrintComponentPresenter(appStateManager, userSettingsManager, encryptUserLoginInfoInteractor, saveUserLoginInfoInteractor, testLoginInteractor)
    }

    @Test
    fun `Test Load User State`() {

        every { appStateManager.state } returns AppState(currentSettings = UserSettings(lastLoginProviderId = "email", id = 1))
        // To init loadUserState()
        presenter.onViewCreated(view, lifecycle)
        verify { view.setProviderMode(LoginInfoType.EMAIL) }

        every { appStateManager.state } returns AppState(currentSettings = UserSettings(1, lastLoginProviderId = ""))
        presenter.onViewCreated(view, lifecycle)
        verify { view.setProviderMode(LoginInfoType.SOCIAL_SECURITY) }

        every { appStateManager.state } returns AppState()
        presenter.onViewCreated(view, lifecycle)
        verify {
            val errorMessage = ViewError(
                    Translation.error.genericTitle, Translation.error.genericMessage,
                    shouldDisplay = true,
                    shouldCloseView = true
            )
            view.showErrorDialog(errorMessage)
        }
    }

    @Test
    fun `Verify Login Credentials Test`() {
        val loginInfo = LoginInfo()
        val user = User(1)
        val appState = AppState(currentUser = user)
        val code = "activation_code"

        every { view.getUserLoginInfo() } returns loginInfo
        every { userSettingsManager[any()].activationCode } returns code
        every { appStateManager.state } returns appState

        presenter.onViewCreated(view, lifecycle)
        presenter.verifyLoginCredentials()

        verify {
            view.showProgress(true)
            testLoginInteractor.input = TestLoginInteractor.Input(loginInfo.socialSecurity, loginInfo.password, code)
            testLoginInteractor.run()
        }
    }

    @Test
    fun `Test Encrypt User Info`() {
        val loginInfo = LoginInfo()
        val user = User(1)
        val appState = AppState(currentUser = user)
        val code = "activation_code"

        every { view.getUserLoginInfo() } returns loginInfo
        every { userSettingsManager[any()].activationCode } returns code
        every { appStateManager.state } returns appState

        presenter.onViewCreated(view, lifecycle)
        presenter.encryptUserLoginInfo()

        verify {
            encryptUserLoginInfoInteractor.input = EncryptUserLoginInfoInteractor.Input(loginInfo)
            encryptUserLoginInfoInteractor.run()
        }
    }

    @Test
    fun `Test On Error`() {
        every { appStateManager.state } returns AppState()
        presenter.onViewCreated(view, lifecycle)

        val error = ViewError()
        presenter.onError(error)
        presenter.onSaveUserError(error)
        presenter.onTestLoginDenied(error)
        presenter.onTestLoginError(error)

        verify(exactly = 4) {
            view.showProgress(false)
            view.showErrorDialog(error)
        }
    }
}