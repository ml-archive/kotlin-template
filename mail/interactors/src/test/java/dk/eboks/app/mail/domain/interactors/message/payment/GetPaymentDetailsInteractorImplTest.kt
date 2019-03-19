package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetPaymentDetailsInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = GetPaymentDetailsInteractorImpl(executor, repository)

    @Test
    fun `Test Get Payment Details`() {
        val latch = CountDownLatch(1)

        every { repository.getPaymentDetails(any(), any()) } returns mockk(relaxed = true)

        interactor.input = GetPaymentDetailsInteractor.Input(1, "id")
        interactor.output = object : GetPaymentDetailsInteractor.Output {
            override fun onPaymentDetailsLoaded(payment: Payment) {
                assert(true)
                latch.countDown()
            }

            override fun onPaymentDetailsLoadingError(viewError: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Get Payment Details Error`() {
        val latch = CountDownLatch(1)

        every { repository.getPaymentDetails(any(), any()) } throws Exception()

        interactor.input = GetPaymentDetailsInteractor.Input(1, "id")
        interactor.output = object : GetPaymentDetailsInteractor.Output {
            override fun onPaymentDetailsLoaded(payment: Payment) {
                assert(false)
                latch.countDown()
            }

            override fun onPaymentDetailsLoadingError(viewError: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}