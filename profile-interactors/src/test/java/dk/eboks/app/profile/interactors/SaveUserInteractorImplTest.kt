package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class SaveUserInteractorImplTest {

    private val executor = TestExecutor()
    private val userManager: UserManager = mockk(relaxUnitFun = true)

    private val interactor = SaveUserInteractorImpl(executor, userManager)

    @Test
    fun `Save User Success Test`() {

        val latch = CountDownLatch(1)
        val user = User()

        every { userManager.put(user) } returns user
        every { userManager.users } returns mutableListOf()

        interactor.input = SaveUserInteractor.Input(user)
        interactor.output = object : SaveUserInteractor.Output {
            override fun onSaveUser(user: User, numberOfUsers: Int) {
                assert(true)
                latch.countDown()
            }

            override fun onSaveUserError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Save User Success Error`() {

        val latch = CountDownLatch(1)
        val user = User()

        every { userManager.put(user) } throws Exception()
        every { userManager.users } returns mutableListOf()

        interactor.input = SaveUserInteractor.Input(user)
        interactor.output = object : SaveUserInteractor.Output {
            override fun onSaveUser(user: User, numberOfUsers: Int) {
                assert(false)
                latch.countDown()
            }

            override fun onSaveUserError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}