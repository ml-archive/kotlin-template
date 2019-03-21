package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.UserRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Test
import java.util.concurrent.CountDownLatch

class ConfirmPhoneInteractorImplTest {

    private val executor = TestExecutor()
    private val userRepo = mockk<UserRepository>(relaxUnitFun = true)
    private val interactor = ConfirmPhoneInteractorImpl(executor, userRepo)

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Test success`() {
        interactor.input = ConfirmPhoneInteractor.Input("", "")
        val latch = CountDownLatch(1)
        interactor.output = object : ConfirmPhoneInteractor.Output {
            override fun onConfirmPhone() {
                latch.countDown()
            }

            override fun onConfirmPhoneError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test failure`() {
        interactor.input = ConfirmPhoneInteractor.Input("", "")
        every { userRepo.confirmPhone(any(), any()) } throws RuntimeException()
        val latch = CountDownLatch(1)
        interactor.output = object : ConfirmPhoneInteractor.Output {
            override fun onConfirmPhone() {
                assert(false)
                latch.countDown()
            }

            override fun onConfirmPhoneError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args`() {
        val latch = CountDownLatch(1)
        interactor.output = object : ConfirmPhoneInteractor.Output {
            override fun onConfirmPhone() {
                assert(false)
                latch.countDown()
            }

            override fun onConfirmPhoneError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}