package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class SaveUsersInteractorImplTest {

    private val executor = TestExecutor()
    private val userManager: UserManager = mockk(relaxUnitFun = true)

    private val interactor = SaveUsersInteractorImpl(executor, userManager)

    @Test
    fun `Test Save Users`() {
        val latch = CountDownLatch(1)
        interactor.output = object : SaveUsersInteractor.Output {
            override fun onSaveUsers() {
                assert(true)
                latch.countDown()
            }

            override fun onSaveUsersError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Save Users Error`() {
        val latch = CountDownLatch(1)
        every { userManager.put(any()) } throws Exception()
        interactor.output = object : SaveUsersInteractor.Output {
            override fun onSaveUsers() {
                assert(false)
                latch.countDown()
            }

            override fun onSaveUsersError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

}