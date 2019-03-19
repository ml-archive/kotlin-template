package dk.eboks.app.keychain.interactors.signup

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.SignupRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.concurrent.CountDownLatch

class CheckSignupMailInteractorImplTest {
    private val executor = TestExecutor()
    private val signUpRestRepo = mockk<SignupRepository>()

    @Test
    fun `Test Signup exists`() {
        val interactor = CheckSignupMailInteractorImpl(executor, signUpRestRepo)
        every { signUpRestRepo.verifySignupMail(any()) } returns true
        val latch = CountDownLatch(1)
        interactor.input = CheckSignupMailInteractor.Input("test@test.com")
        interactor.output = object : CheckSignupMailInteractor.Output {
            override fun onVerifySignupMail(exists: Boolean) {
                assert(exists)
                latch.countDown()
            }

            override fun onVerifySignupMailError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Signup doesn't exists`() {
        val interactor = CheckSignupMailInteractorImpl(executor, signUpRestRepo)
        every { signUpRestRepo.verifySignupMail(any()) } returns false
        val latch = CountDownLatch(1)
        interactor.input = CheckSignupMailInteractor.Input("test@test.com")
        interactor.output = object : CheckSignupMailInteractor.Output {
            override fun onVerifySignupMail(exists: Boolean) {
                assertFalse(exists)
                latch.countDown()
            }

            override fun onVerifySignupMailError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Signup exception`() {
        val interactor = CheckSignupMailInteractorImpl(executor, signUpRestRepo)
        every { signUpRestRepo.verifySignupMail(any()) } throws RuntimeException()
        val latch = CountDownLatch(1)
        interactor.input = CheckSignupMailInteractor.Input("test@test.com")
        interactor.output = object : CheckSignupMailInteractor.Output {
            override fun onVerifySignupMail(exists: Boolean) {
                assert(false)
                latch.countDown()
            }

            override fun onVerifySignupMailError(error: ViewError) {

                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args`() {
        val interactor = CheckSignupMailInteractorImpl(executor, signUpRestRepo)
        val latch = CountDownLatch(1)
        interactor.output = object : CheckSignupMailInteractor.Output {
            override fun onVerifySignupMail(exists: Boolean) {
                assert(false)
                latch.countDown()
            }

            override fun onVerifySignupMailError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}