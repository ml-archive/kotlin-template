package dk.eboks.app.keychain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetUsersInteractorImplTest {

    private val executor = TestExecutor()
    private val userManager = mockk<UserManager>()
    private val interactor = GetUsersInteractorImpl(executor, userManager)

    @Test
    fun `Test success`() {
        val userListMock = mutableListOf(User(id = 135), User(id = 3156))
        every { userManager.users } returns userListMock
        val latch = CountDownLatch(1)
        interactor.output = object : GetUsersInteractor.Output {
            override fun onGetUsers(users: MutableList<User>) {
                assertEquals(users, userListMock)
                latch.countDown()
            }

            override fun onGetUsersError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test failure`() {
        every { userManager.users } throws Exception()
        val latch = CountDownLatch(1)
        interactor.output = object : GetUsersInteractor.Output {
            override fun onGetUsers(users: MutableList<User>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetUsersError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}