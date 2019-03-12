package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.UserRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class VerifyPhoneInteractorImplTest {

    private val executor = TestExecutor()
    private val repository: UserRepository = mockk(relaxUnitFun = true)

    private val interactor = VerifyPhoneInteractorImpl(executor, repository)

    @Test
    fun `Test Verify Phone`() {
        val latch = CountDownLatch(1)
        val phone = "41211312312"

        interactor.input = VerifyPhoneInteractor.Input(phone)
        interactor.output = object : VerifyPhoneInteractor.Output {

            override fun onVerifyPhone() {
                assert(true)
                latch.countDown()
            }

            override fun onVerifyPhoneError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Verify Phone Error`() {
        val latch = CountDownLatch(1)
        val phone = "12312312"

        every { repository.verifyPhone(any()) } throws Exception()

        interactor.input = VerifyPhoneInteractor.Input(phone)
        interactor.output = object : VerifyPhoneInteractor.Output {

            override fun onVerifyPhone() {
                assert(false)
                latch.countDown()
            }

            override fun onVerifyPhoneError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}