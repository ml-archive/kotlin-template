package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class TogglePaymentNotificationsInteractorImplTest {

    private val executor = TestExecutor()
    private val api = mockk<Api>(relaxUnitFun = true)

    private val interactor = TogglePaymentNotificationInteractorImpl(executor, api)

    @Test
    fun `Payment Notifications Toggle Test`() {
        val latch = CountDownLatch(1)

        every {
            api.togglePaymentNotifications(any(), any(), any()).execute()
        } returns Response.success(null)

        interactor.input = TogglePaymentNotificationInteractor.Input(1, "", true)
        interactor.output = object : TogglePaymentNotificationInteractor.Output {
            override fun onNotificationsToggleUpdated(newValue: Boolean) {
                assert(true)
                latch.countDown()
            }

            override fun onNotificationToggleUpdateError(viewError: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Payment Notifications Toggle Error Test`() {
        val latch = CountDownLatch(1)

        every { api.togglePaymentNotifications(any(), any(), any()).execute() } throws Exception()

        interactor.input = TogglePaymentNotificationInteractor.Input(1, "", true)
        interactor.output = object : TogglePaymentNotificationInteractor.Output {
            override fun onNotificationsToggleUpdated(newValue: Boolean) {
                assert(false)
                latch.countDown()
            }

            override fun onNotificationToggleUpdateError(viewError: ViewError) {
                assert(true)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}