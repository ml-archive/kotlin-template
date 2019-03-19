package dk.eboks.app.keychain.interactors.user

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.UserRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch

class CheckSsnExistsInteractorImplTest {

    private val executor = TestExecutor()
    private val userRepo = mockk<UserRepository>()

    @Test
    fun `Test check SSN exists`() {
        val ssn = "AS54as456a46s"
        val interactor = CheckSsnExistsInteractorImpl(executor, userRepo)
        every { userRepo.checkSsn(ssn) } returns true
        val latch = CountDownLatch(1)
        interactor.input = CheckSsnExistsInteractor.Input(ssn)
        interactor.output = object : CheckSsnExistsInteractor.Output {
            override fun onCheckSsnExists(exists: Boolean) {
                assert(exists)
                latch.countDown()
            }

            override fun onCheckSsnExistsError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test check SSN doesn't exists`() {
        val ssn = "AS54as456a46s"
        val interactor = CheckSsnExistsInteractorImpl(executor, userRepo)
        every { userRepo.checkSsn(ssn) } returns false
        val latch = CountDownLatch(1)
        interactor.input = CheckSsnExistsInteractor.Input(ssn)
        interactor.output = object : CheckSsnExistsInteractor.Output {
            override fun onCheckSsnExists(exists: Boolean) {
                Assert.assertFalse(exists)
                latch.countDown()
            }

            override fun onCheckSsnExistsError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test check SSN exception`() {
        val interactor = CheckSsnExistsInteractorImpl(executor, userRepo)
        every { userRepo.checkSsn(any()) } throws Exception()
        val latch = CountDownLatch(1)
        interactor.input = CheckSsnExistsInteractor.Input("test@test.com")
        interactor.output = object : CheckSsnExistsInteractor.Output {
            override fun onCheckSsnExists(exists: Boolean) {
                assert(false)
                latch.countDown()
            }

            override fun onCheckSsnExistsError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args`() {
        val interactor = CheckSsnExistsInteractorImpl(executor, userRepo)
        val latch = CountDownLatch(1)
        interactor.output = object : CheckSsnExistsInteractor.Output {
            override fun onCheckSsnExists(exists: Boolean) {
                assert(false)
                latch.countDown()
            }

            override fun onCheckSsnExistsError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}