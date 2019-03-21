package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class GetUserProfileInteractorImplTest {
    private val executor = TestExecutor()
    private val api = mockk<Api>(relaxUnitFun = true)
    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val userManager = mockk<UserManager>(relaxUnitFun = true)
    private val userSettingsManager = mockk<UserSettingsManager>(relaxUnitFun = true)
    private val interactor = GetUserProfileInteractorImpl(
        executor,
        api,
        appStateManager,
        userManager,
        userSettingsManager
    )

    @After
    fun tearDown() {
        interactor.output = null
    }

    @Test
    fun `Test success`() {
        val userMock = User(id = 15)
        every { api.getUserProfile().execute() } returns Response.success(userMock)
        every { appStateManager.state } returns AppState()
        every { userManager.put(any()) } returns userMock
        every { userSettingsManager[15] } returns UserSettings(15)
        val latch = CountDownLatch(1)
        interactor.output = object : GetUserProfileInteractor.Output {
            override fun onGetUser(user: User) {
                Assert.assertEquals(user, userMock)
                latch.countDown()
            }

            override fun onGetUserError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        verify {
            userManager.put(userMock)
            appStateManager.save()
        }
        latch.await()
    }

    @Test
    fun `Test failure`() {
        every { api.getUserProfile().execute() } throws Exception()
        val latch = CountDownLatch(1)
        interactor.output = object : GetUserProfileInteractor.Output {
            override fun onGetUser(user: User) {
                assert(false)

                latch.countDown()
            }

            override fun onGetUserError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}