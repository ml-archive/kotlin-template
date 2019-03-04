package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.network.util.errorBodyToViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class ResetPasswordInteractorImplTest {
    private val api = mockk<Api>()
    private val appConfig = mockk<AppConfig>()
    private val executor = TestExecutor()

    @Test
    fun `Test reset success`() {
        val interactor = ResetPasswordInteractorImpl(executor, api, appConfig)
        interactor.input = ResetPasswordInteractor.Input("test@test.com")
        every { appConfig.currentMode.countryCode } returns "DK"
        every { api.resetPassword(any(), any()).execute() } returns Response.success(null)
        val latch = CountDownLatch(1)
        interactor.output = object : ResetPasswordInteractor.Output {
            override fun onSuccess() {

                latch.countDown()
            }

            override fun onError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.execute()
        latch.await()
    }

    @Test
    fun `Test reset fail`() {
        val interactor = ResetPasswordInteractorImpl(executor, api, appConfig)
        interactor.input = ResetPasswordInteractor.Input("test@test.com")
        every { appConfig.currentMode.countryCode } returns "DK"
        val responseError = Response.error<Void>(
            400,
            ResponseBody.create(
                MediaType.parse("application/json"),
                "{}"
            )
        )
        every { api.resetPassword(any(), any()).execute() } returns responseError

        val latch = CountDownLatch(1)
        interactor.output = object : ResetPasswordInteractor.Output {
            override fun onSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onError(error: ViewError) {
                assertEquals(error, responseError.errorBodyToViewError())
                latch.countDown()
            }
        }
        interactor.execute()
        latch.await()
    }
}