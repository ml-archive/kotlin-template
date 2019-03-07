package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class GetLatestUploadsInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()
    private val interactor = GetLatestUploadsInteractorImpl(executor, repository)

    @Test
    fun `Get Latest Uploads Test`() {

        val latch = CountDownLatch(1)
        every { repository.getLatestUploads(any(), any()) } returns listOf()

        interactor.input = GetLatestUploadsInteractor.Input()
        interactor.output = object : GetLatestUploadsInteractor.Output {
            override fun onGetLatestUploads(messages: List<Message>) {
                assert(true)
                assert(messages.isEmpty())
                latch.countDown()
            }

            override fun onGetLatestUploadsError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Latest Uploads Error Test`() {
        val latch = CountDownLatch(1)
        every { repository.getLatestUploads(any(), any()) } throws Exception()

        interactor.input = GetLatestUploadsInteractor.Input()
        interactor.output = object : GetLatestUploadsInteractor.Output {
            override fun onGetLatestUploads(messages: List<Message>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetLatestUploadsError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}