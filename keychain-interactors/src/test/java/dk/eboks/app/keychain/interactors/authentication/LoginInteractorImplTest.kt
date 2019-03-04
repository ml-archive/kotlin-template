package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.AuthException
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class LoginInteractorImplTest {
    private val executor = TestExecutor()
    private val api = mockk<Api>()
    private val appStateManager = mockk<AppStateManager>()
    private val userManager = mockk<UserManager>()
    private val userSettingsManager = mockk<UserSettingsManager>()
    private val authClient = mockk<AuthClient>()
    private val cacheManager = mockk<CacheManager>()
    private val foldersRepositoryMail = mockk<MailCategoriesRepository>()
    private val interactor = LoginInteractorImpl(
        executor,
        api,
        appStateManager,
        userManager,
        userSettingsManager,
        authClient,
        cacheManager,
        foldersRepositoryMail
    )

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Test Login success`() {
        val user = User()
        val userSettings = UserSettings(0)
        every { api.getUserProfile().execute() } returns Response.success(user)
        every { userSettingsManager[any()] } returns userSettings
        every { authClient.login(any(), any(), any(), any(), any(), any()) } returns AccessToken(
            "",
            0,
            "",
            ""
        )
        every { appStateManager.state } returns AppState()
        every { userManager.put(any()) } returns user
        every { cacheManager.clearStores() } returns Unit
        every { userSettingsManager.put(any()) } returns userSettings
        every { appStateManager.save() } returns Unit
        every { foldersRepositoryMail.getMailCategories(any(), any()) } returns listOf()

        val latch = CountDownLatch(1)
        interactor.input = LoginInteractor.Input(LoginState())
        interactor.output = object : LoginInteractor.Output {
            override fun onLoginSuccess(response: AccessToken) {
                assertTrue(true)
                latch.countDown()
            }

            override fun onLoginActivationCodeRequired() {
                assertTrue(false)
                latch.countDown()
            }

            override fun onLoginDenied(error: ViewError) {
                assertTrue(false)
                latch.countDown()
            }

            override fun onLoginError(error: ViewError) {
                assertTrue(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Login denied`() {
        val latch = CountDownLatch(1)
        val user = User()
        val userSettings = UserSettings(0)
        every { api.getUserProfile().execute() } returns Response.success(user)
        every { userSettingsManager[any()] } returns userSettings
        every { authClient.login(any(), any(), any(), any(), any(), any()) } throws AuthException(
            400,
            ""
        )
        interactor.input = LoginInteractor.Input(LoginState())
        interactor.output = object : LoginInteractor.Output {
            override fun onLoginSuccess(response: AccessToken) {
                assertTrue(false)
                latch.countDown()
            }

            override fun onLoginActivationCodeRequired() {
                assertTrue(false)
                latch.countDown()
            }

            override fun onLoginDenied(error: ViewError) {
                assertTrue(true)
                latch.countDown()
            }

            override fun onLoginError(error: ViewError) {
                assertTrue(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}