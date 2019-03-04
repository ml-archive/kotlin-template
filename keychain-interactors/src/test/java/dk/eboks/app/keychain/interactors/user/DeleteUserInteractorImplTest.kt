package dk.eboks.app.keychain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyForUserInteractor
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch

class DeleteUserInteractorImplTest {

    private val executor = TestExecutor()
    private val userManager = mockk<UserManager>()
    private val userSettingsManager = mockk<UserSettingsManager>()
    private val deleteRSAKeyForUserInteractor = mockk<DeleteRSAKeyForUserInteractor>()
    private val interactor = DeleteUserInteractorImpl(
        executor,
        userManager,
        userSettingsManager,
        deleteRSAKeyForUserInteractor
    )

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Delete user success`() {
        val testUser = User(id = 12354)
        every { userManager.remove(testUser) } returns Unit
        every { userSettingsManager.remove(UserSettings(testUser.id)) } returns Unit
        every { deleteRSAKeyForUserInteractor.run() } returns Unit
        every { deleteRSAKeyForUserInteractor.input = any() } returns Unit
        interactor.input = DeleteUserInteractor.Input(testUser)
        val latch = CountDownLatch(1)
        interactor.output = object : DeleteUserInteractor.Output {
            override fun onDeleteUserError(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onDeleteUser(user: User) {
                Assert.assertEquals(user, testUser)
                latch.countDown()
            }
        }
        interactor.run()
    }

    @Test
    fun `Delete user failure`() {
        val testUser = User(id = 12354)
        every { userManager.remove(testUser) } returns Unit
        every { userSettingsManager.remove(UserSettings(testUser.id)) } returns Unit
        every { deleteRSAKeyForUserInteractor.run() } returns Unit
        every { deleteRSAKeyForUserInteractor.input = any() } returns Unit

        val latch = CountDownLatch(1)
        interactor.output = object : DeleteUserInteractor.Output {
            override fun onDeleteUserError(error: ViewError) {
                latch.countDown()
            }

            override fun onDeleteUser(user: User) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
    }
}