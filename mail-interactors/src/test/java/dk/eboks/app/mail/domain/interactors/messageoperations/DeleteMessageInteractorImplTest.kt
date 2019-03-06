package dk.eboks.app.mail.domain.interactors.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList
import kotlin.random.Random

class DeleteMessageInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = DeleteMessagesInteractorImpl(executor, repository)



    @Test
    fun `Delete Messages Test`() {

        val latch = CountDownLatch(1)
        val messages = List(12) {Message(it.toString(), "s", Date(), false, folderId = 12)}

        every { repository.deleteMessage(any(),any()) } returns Unit

        interactor.input = DeleteMessagesInteractor.Input(ArrayList(messages))
        interactor.output = object : DeleteMessagesInteractor.Output {
            override fun onDeleteMessagesSuccess() {
                assert(true)
                latch.countDown()
            }

            override fun onDeleteMessagesError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }

    @Test
    fun `Delete Messages Error Test`() {
        val latch = CountDownLatch(1)
        val messages = List(12) {Message(it.toString(), "s", Date(), false, folderId = 12)}

        every { repository.deleteMessage(any(), any()) } throws Exception()

        interactor.input = DeleteMessagesInteractor.Input(ArrayList(messages))
        interactor.output = object : DeleteMessagesInteractor.Output {
            override fun onDeleteMessagesSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onDeleteMessagesError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }
}