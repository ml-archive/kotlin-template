package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetPaymentLinkInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = GetPaymentLinkInteractorImpl(executor, repository)

    @Test
    fun `Get Payment Link Test`() {
        val latch = CountDownLatch(1)
        every { repository.getPaymentLink(any(), any(), any()) } returns mockk(relaxed = true)

        interactor.input = GetPaymentLinkInteractor.Input("", 12, "type")
        interactor.output = object : GetPaymentLinkInteractor.Output {
            override fun onPaymentLinkLoaded(link: Link) {
                assert(true)
                latch.countDown()
            }

            override fun onPaymentLinkLoadingError(viewError: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }

    @Test
    fun `Get Payment Link Error Test`() {
        val latch = CountDownLatch(1)
        every { repository.getPaymentLink(any(), any(), any()) } throws Exception()

        interactor.input = GetPaymentLinkInteractor.Input("", 12, "type")
        interactor.output = object : GetPaymentLinkInteractor.Output {
            override fun onPaymentLinkLoaded(link: Link) {
                assert(false)
                latch.countDown()
            }

            override fun onPaymentLinkLoadingError(viewError: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }
}