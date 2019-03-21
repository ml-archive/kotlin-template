package dk.eboks.app.presentation.ui.channels.components.content.ekey.open

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.locksmith.core.preferences.EncryptedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class EkeyOpenItemComponentPresenterTest {

    private lateinit var presenter: EkeyOpenItemComponentPresenter
    private val appState = mockk<AppStateManager>(relaxUnitFun = true)
    private val encryptedPreferences = mockk<EncryptedPreferences>(relaxUnitFun = true)
    private val setEKeyVaultInteractor = mockk<SetEKeyVaultInteractor>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)
    private val viewMock = mockk<EkeyOpenItemComponentContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = EkeyOpenItemComponentPresenter(
            appState,
            encryptedPreferences,
            setEKeyVaultInteractor
        )
        presenter.onViewCreated(viewMock, lifecycle)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test auth error`() {
        val user = User(1234)
        every { appState.state?.currentUser }.returns(user)
        presenter.onAuthError(1)
        verify {
            encryptedPreferences.remove("ekey_${user.id}")
            viewMock.showPinView()
        }
    }

    @Test
    fun `Test set EKey vault success`() {
        presenter.onSetEKeyVaultSuccess()
        verify { viewMock.onSuccess() }
    }

    @Test
    fun `Test set EKey vault error`() {
        val viewError = ViewError()
        presenter.onSetEKeyVaultError(viewError)
        verify { viewMock.showErrorDialog(viewError) }
    }
}