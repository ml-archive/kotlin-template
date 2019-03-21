package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.UserRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class VerifyEmailInteractorImplTest {

    private val executor = TestExecutor()
    private val repository: UserRepository = mockk(relaxUnitFun = true)

    private val interactor = VerifyEmailInteractorImpl(executor, repository)

    @Test
    fun `Test Verify Email`() {
        val latch = CountDownLatch(1)
        val email = "role@nodesagency.com"

        interactor.input = VerifyEmailInteractor.Input(email)
        interactor.output = object : VerifyEmailInteractor.Output {
            override fun onVerifyMail() {
                assert(true)
                latch.countDown()
            }

            override fun onVerifyMailError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Verify Email Error`() {
        val latch = CountDownLatch(1)
        val email = "role@nodesagency.com"

        every { repository.verifyEmail(any()) } throws Exception()

        interactor.input = VerifyEmailInteractor.Input(email)
        interactor.output = object : VerifyEmailInteractor.Output {
            override fun onVerifyMail() {
                assert(false)
                latch.countDown()
            }

            override fun onVerifyMailError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}