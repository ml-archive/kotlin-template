package dk.eboks.app.presentation.ui.channels.components.content.ekey.detail

import android.util.Base64
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
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Base64::class)
class EkeyDetailComponentPresenterTest {

    private lateinit var presenter: EkeyDetailComponentPresenter
    private val appState = mockk<AppStateManager>(relaxUnitFun = true)
    private val encryptedPreferences = mockk<EncryptedPreferences>(relaxUnitFun = true)
    private val setEKeyVaultInteractor = mockk<SetEKeyVaultInteractor>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)
    private val viewMock = mockk<EkeyDetailComponentContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = EkeyDetailComponentPresenter(
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
// TODO
//    @Test
//    fun putVault() {
//        val user = User(12345)
//        val masterKey = "masterKey"
//        mockStatic(Base64::class.java)
//        whenever(Base64.encodeToString(any(), anyInt())).thenReturn("encoded")
//        whenever(Base64.encodeToString(any(), anyInt(), anyInt(), anyInt())).thenReturn("encoded")
//        every { appState.state?.currentUser } returns user
//        every { encryptedPreferences.getString("ekey_${user.id}", any()) } returns masterKey
//        val mutableList = mutableListOf<BaseEkey>()
//        val ekey = Login(
//            username = "username",
//            password = "password",
//            name = "name",
//            note = "note"
//        )
//        presenter.putVault(mutableList, ekey)
//        verify {
//            viewMock.showLoading(true)
//            setEKeyVaultInteractor.input =
//                SetEKeyVaultInteractor.Input(any(), any(), any(), 0)
//            setEKeyVaultInteractor.run()
//        }
//    }

    @Test
    fun `Test set EKey vault success`() {
        presenter.onSetEKeyVaultSuccess()
        verify { viewMock.onSuccess() }
    }

    @Test
    fun `Test auth error`() {
        val user = User(1234)
        every { appState.state?.currentUser }.returns(user)
        presenter.onAuthError(1)
        verify {
            encryptedPreferences.remove("ekey_${user.id}")
            viewMock.showPinView()
            viewMock.showLoading(false)
        }
    }

    @Test
    fun `Test set EKey vault error`() {
        val viewError = ViewError()
        presenter.onSetEKeyVaultError(viewError)
        verify {
            viewMock.showErrorDialog(viewError)
            viewMock.showLoading(false)
        }
    }
}