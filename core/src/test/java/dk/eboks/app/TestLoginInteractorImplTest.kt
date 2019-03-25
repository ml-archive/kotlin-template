package dk.eboks.app

import dk.eboks.app.domain.interactors.TestLoginInteractor
import dk.eboks.app.domain.interactors.TestLoginInteractorImpl
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.AuthException
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class TestLoginInteractorImplTest {

    private val executor = TestExecutor()
    private val authClient: AuthClient = mockk(relaxUnitFun = true)

    private val interactor = TestLoginInteractorImpl(executor, authClient)

    @Test
    fun `Test Login Success`() {
        val latch = CountDownLatch(1)

        every { authClient.login(any(), any(), any(), any(), any(), any()) } returns mockk(relaxed = true)

        interactor.input = TestLoginInteractor.Input("user", "pass", "activation")
        interactor.output = object : TestLoginInteractor.Output {
            override fun onTestLoginSuccess() {
                assert(true)
                latch.countDown()
            }

            override fun onTestLoginDenied(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onTestLoginError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Login Ivalid Credentials Error`() {
        val latch = CountDownLatch(1)
        val invalidError = ViewError(
                title = Translation.error.genericTitle,
                message = Translation.error.genericMessage,
                shouldCloseView = false
        )
        every { authClient.login(any(), any(), any(), any(), any(), any()) } throws AuthException(httpCode = 400, errorDescription = "User verification error")

        interactor.input = TestLoginInteractor.Input("user", "pass", "activation")
        interactor.output = object : TestLoginInteractor.Output {
            override fun onTestLoginSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onTestLoginDenied(error: ViewError) {
                assert(true)
                latch.countDown()
            }

            override fun onTestLoginError(error: ViewError) {
                assert(false)
                assert(error == invalidError)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Login Generic Error`() {
        val latch = CountDownLatch(1)

        every { authClient.login(any(), any(), any(), any(), any(), any()) } throws Exception()

        interactor.input = TestLoginInteractor.Input("user", "pass", "activation")
        interactor.output = object : TestLoginInteractor.Output {
            override fun onTestLoginSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onTestLoginDenied(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onTestLoginError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}
