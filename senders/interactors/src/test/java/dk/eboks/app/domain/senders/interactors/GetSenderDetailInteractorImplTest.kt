package dk.eboks.app.domain.senders.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.repositories.SendersRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetSenderDetailInteractorImplTest {

    private val executor = TestExecutor()
    private val repository: SendersRepository = mockk()

    private val interactor: GetSenderDetailInteractor = GetSenderDetailInteractorImpl(executor, repository)

    @Test
    fun `Test Sender Detail Loading Success`() {
        val latch = CountDownLatch(1)
        every { repository.getSenderDetail(any()) } returns mockk(relaxed = true)

        interactor.input = GetSenderDetailInteractor.Input(1)
        interactor.output = object : GetSenderDetailInteractor.Output {
            override fun onGetSender(sender: Sender) {
                assert(true)
                latch.countDown()
            }

            override fun onGetSenderError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Sender Detail Loading Error`() {
        val latch = CountDownLatch(1)
        every { repository.getSenderDetail(any()) } throws Exception()

        interactor.input = GetSenderDetailInteractor.Input(1)
        interactor.output = object : GetSenderDetailInteractor.Output {
            override fun onGetSender(sender: Sender) {
                assert(false)
                latch.countDown()
            }

            override fun onGetSenderError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}