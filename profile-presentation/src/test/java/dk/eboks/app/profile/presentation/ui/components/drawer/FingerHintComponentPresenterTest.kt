package dk.eboks.app.profile.presentation.ui.components.drawer

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.profile.interactors.SaveUserInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FingerHintComponentPresenterTest {

    private val appStateManger: AppStateManager = mockk(relaxUnitFun = true)
    private val userSettingsManager: UserSettingsManager = mockk(relaxUnitFun = true)
    private val encryptUserInfoInteractor: EncryptUserLoginInfoInteractor = mockk(relaxUnitFun = true)
    private val saveUserInteractor: SaveUserInteractor = mockk(relaxUnitFun = true)

    private val view: FingerHintComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: FingerHintComponentPresenter

    private val currentUser = User(id = 2)
    private val loginInfo = LoginInfo()

    @Before
    fun setUp() {
        every { appStateManger.state } returns AppState(currentUser = currentUser)
        every { userSettingsManager[currentUser.id] } returns UserSettings(2)
        presenter = FingerHintComponentPresenter(appStateManger, userSettingsManager, encryptUserInfoInteractor, saveUserInteractor)
        presenter.onViewCreated(view, lifecycle)
    }

    @Test
    fun `Test Encrypt User Info`() {
        every { view.getUserLoginInfo() } returns loginInfo
        presenter.encryptUserLoginInfo()

        verify {
            encryptUserInfoInteractor.input = EncryptUserLoginInfoInteractor.Input(loginInfo)
            encryptUserInfoInteractor.run()
        }
    }

    @Test
    fun `Test Finger Print Enrollment Success`() {

        presenter.onSuccess()
        verify {

            userSettingsManager.save()

            saveUserInteractor.input = SaveUserInteractor.Input(currentUser)
            saveUserInteractor.run()
        }
    }

    @Test
    fun `Test Finger Print Enrollment Failure`() {

        // Current user is null
        every { appStateManger.state } returns AppState(currentUser = null)
        presenter.onSuccess()
        verify {
            val viewError = ViewError(Translation.error.genericTitle, Translation.error.genericMessage, true, true)
            view.showErrorDialog(viewError)
        }
    }

    @Test
    fun `Test Error`() {
        val viewError = ViewError()
        presenter.onError(viewError)

        presenter.onSaveUserError(viewError)

        verify(exactly = 2) { view.showErrorDialog(viewError) }
    }
}