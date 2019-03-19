package dk.eboks.app.mail.domain.interactors.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch
import kotlin.random.Random

class MoveMessagesInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = MoveMessagesInteractorImpl(executor, repository)

    @Test
    fun `Move Messages Test`() {

        val numOfMessages = Random.nextInt(10)
        val latch = CountDownLatch(numOfMessages)

        val messages = List(numOfMessages) { mockk<Message>() }

        every { repository.updateMessage(any(), any()) } returns Unit

        interactor.input = MoveMessagesInteractor.Input(12, ArrayList(messages))
        interactor.output = object : MoveMessagesInteractor.Output {
            override fun onMoveMessagesSuccess() {
                assert(true)
                latch.countDown()
            }

            override fun onMoveMessagesError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Move Messages Error Test`() {

        val numOfMessages = Random.nextInt(10)
        val latch = CountDownLatch(1)

        val messages = List(numOfMessages) { mockk<Message>() }

        every { repository.updateMessage(any(), any()) } throws Exception()

        interactor.input = MoveMessagesInteractor.Input(12, ArrayList(messages))
        interactor.output = object : MoveMessagesInteractor.Output {
            override fun onMoveMessagesSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onMoveMessagesError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}