package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.keychain.interactors.mobileacces.ActivateDeviceInteractor
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyInteractor
import dk.eboks.app.keychain.interactors.mobileacces.GenerateRSAKeyInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeviceActivationComponentPresenterTest {

    private val appState = mockk<AppStateManager>(relaxUnitFun = true)
    private val generateRSAKeyInteractor = mockk<GenerateRSAKeyInteractor>(relaxUnitFun = true)
    private val activateDeviceInteractor = mockk<ActivateDeviceInteractor>(relaxUnitFun = true)
    private val deleteRSAKeyInteractor = mockk<DeleteRSAKeyInteractor>(relaxUnitFun = true)
    private lateinit var presenter: DeviceActivationComponentPresenter
    private val viewMock = mockk<DeviceActivationComponentContract.View>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = DeviceActivationComponentPresenter(
            appState,
            generateRSAKeyInteractor,
            activateDeviceInteractor,
            deleteRSAKeyInteractor
        )
        presenter.onViewCreated(viewMock, lifecycle)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test request Nemid login`() {
        presenter.requestNemidLogin()
        verify { viewMock.requestNemidLogin() }
    }

    @Test
    fun `Test activate device`() {
        val user = User(10)
        every { appState.state } returns AppState(currentUser = user)
        presenter.activateDevice()
        verify {
            viewMock.showProgress(true)
            generateRSAKeyInteractor.input = GenerateRSAKeyInteractor.Input(user.id.toString())
            generateRSAKeyInteractor.run()
        }
    }

    @Test
    fun `Test generate RSAKey success`() {
        val mockRsaKey = "rsaKey"
        presenter.onGenerateRSAKeySuccess(mockRsaKey)
        verify {
            activateDeviceInteractor.input = ActivateDeviceInteractor.Input(mockRsaKey)
            activateDeviceInteractor.run()
        }
    }

    @Test
    fun `Test generate RSAKey error`() {
        val viewError = ViewError()
        presenter.onGenerateRSAKeyError(viewError)
        verify { viewMock.showProgress(false) }
    }

    @Test
    fun `Test activate device success`() {
        presenter.onActivateDeviceSuccess()
        verify {
            viewMock.showProgress(false)
            viewMock.login()
        }
    }

    @Test
    fun `Test activate device error`() {
        val viewError = ViewError()
        val rsaKey = "rsaKey"
        presenter.onActivateDeviceError(viewError, rsaKey)
        verify {
            deleteRSAKeyInteractor.run()
        }
    }

    @Test
    fun `Test delete RSAKey success`() {
        presenter.onDeleteRSAKeySuccess()
        verify { viewMock.showProgress(false) }
    }

    @Test
    fun `Test delete RSAKey error`() {
        val viewError = ViewError()
        presenter.onDeleteRSAKeyError(viewError)
        verify { viewMock.showProgress(false) }
    }
}