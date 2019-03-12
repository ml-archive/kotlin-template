package dk.eboks.app.profile.presentation.ui.components.main

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.profile.interactors.GetUserProfileInteractor
import dk.eboks.app.profile.interactors.SaveUserInteractor
import dk.eboks.app.profile.interactors.SaveUserSettingsInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class ProfileInfoComponentPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val saveUserInteractor: SaveUserInteractor = mockk(relaxUnitFun = true)
    private val saveUseSettingsInteractor: SaveUserSettingsInteractor = mockk(relaxUnitFun = true)
    private val getUserProfileInteractor: GetUserProfileInteractor = mockk(relaxUnitFun = true)

    private val view: ProfileInfoComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: ProfileInfoComponentPresenter

    private val user = User(verified = false)

    @Before
    fun setUp() {

        every { appStateManager.state } returns AppState(currentUser = user)

        presenter = ProfileInfoComponentPresenter(appStateManager, saveUserInteractor, saveUseSettingsInteractor, getUserProfileInteractor)
        presenter.onViewCreated(view, lifecycle)

        assert(!presenter.isUserVerified)
        verify { view.setName(user.name) }
    }

    @Test
    fun `Load User Data Test`() {
        presenter.loadUserData(false)

        verify {
            getUserProfileInteractor.run()
            view.showProgress(false)
        }

        presenter.loadUserData(true)
        verify {
            getUserProfileInteractor.run()
            view.showProgress(true)
        }
    }

    @Test
    fun `On Get User Test`() {

        val userSettings = UserSettings(1)

        presenter.onGetUser(user)
        verifyOrder {
            view.detachListeners()
            view.setName(user.name)
            view.setVerified(user.verified)
            view.setProfileImage(user.avatarUri)
            view.showProgress(false)
            view.attachListeners()
        }

        every { appStateManager.state } returns AppState(currentUser = user, currentSettings = userSettings)
        presenter.onGetUser(user)

        verifyOrder {
            view.detachListeners()
            view.setName(user.name)
            view.setVerified(user.verified)
            view.setProfileImage(user.avatarUri)

            view.showFingerprintEnabled(userSettings.hasFingerprint, userSettings.lastLoginProviderId)
            view.showKeepMeSignedIn(userSettings.stayLoggedIn)

            view.showProgress(false)
            view.attachListeners()
        }
    }

    @Test
    fun `On Get Error Test`() {
        val viewError = ViewError()
        presenter.onGetUserError(viewError)
        presenter.onSaveUserError(viewError)

        verify(exactly = 2) {
            view.showProgress(false)
            view.showErrorDialog(viewError)
        }
    }

    @Test
    fun `Save User Image Test`() {
        val uri = "content://image/uri"
        val userSettings = UserSettings(1)

        every { appStateManager.state } returns AppState(currentSettings = userSettings, currentUser = user)

        presenter.saveUserImg(uri)

        assert(appStateManager.state?.currentUser?.avatarUri == uri)

        verify {
            appStateManager.save()
            saveUseSettingsInteractor.input = SaveUserSettingsInteractor.Input(userSettings)
            saveUseSettingsInteractor.run()
        }
    }

    @Test
    fun `Enable User Fingerprint Test`() {
        val userSettings = UserSettings(1)
        val enable = Random.nextBoolean()

        every { appStateManager.state } returns AppState(currentSettings = userSettings, currentUser = user)

        presenter.enableUserFingerprint(enable)

        assert(appStateManager.state?.currentSettings?.hasFingerprint == enable)

        verify {
            appStateManager.save()
            saveUseSettingsInteractor.input = SaveUserSettingsInteractor.Input(userSettings)
            saveUseSettingsInteractor.run()
        }
    }

    @Test
    fun `Enable Keep Me Signed In Test`() {
        val userSettings = UserSettings(1)
        val enable = Random.nextBoolean()

        every { appStateManager.state } returns AppState(currentSettings = userSettings, currentUser = user)

        presenter.enableKeepMeSignedIn(enable)

        assert(appStateManager.state?.currentSettings?.stayLoggedIn == enable)

        verify {
            appStateManager.save()
            saveUseSettingsInteractor.input = SaveUserSettingsInteractor.Input(userSettings)
            saveUseSettingsInteractor.run()
        }
    }

    @Test
    fun `Do Logout Test`() {
        val userSettings = UserSettings(1)

        every { appStateManager.state } returns AppState(currentSettings = userSettings, currentUser = user)
        presenter.doLogout()

        verifyOrder {
            appStateManager.save()
            view.logout()
        }

        assert(appStateManager.state?.currentSettings == null)
        assert(appStateManager.state?.loginState?.userPassWord.isNullOrEmpty())
        assert(appStateManager.state?.loginState?.userName.isNullOrEmpty())
        assert(appStateManager.state?.loginState?.token == null)
        assert(appStateManager.state?.openingState?.acceptPrivateTerms == false)
        assert(appStateManager.state?.loginState?.activationCode == null)
    }
}