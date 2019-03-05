package dk.eboks.app.mail.domain.interactors.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class UpdateMessagesInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = UpdateMessageInteractorImpl(executor, repository)

    @Test
    fun `Update Messages Test`() {

        val patches = List(4) {
            when(it) {
                0 -> MessagePatch(unread = true)
                1 -> MessagePatch(archive = true)
                2 -> MessagePatch(folderId = 21)
                else -> MessagePatch(note =  "note")
            }
        }


        every { repository.updateMessage(any(), any()) } returns Unit

        patches.forEach {
            val latch = CountDownLatch(1)

            interactor.input = UpdateMessageInteractor.Input(arrayListOf(mockk()) , it)
            interactor.output = object : UpdateMessageInteractor.Output {
                override fun onUpdateMessageSuccess() {
                    assert(true)
                    latch.countDown()
                }

                override fun onUpdateMessageError(error: ViewError) {
                    assert(false)
                    latch.countDown()
                }

            }

            interactor.run()
            latch.await()
        }


    }


    @Test
    fun `Update Messages Error Test`() {

        val patches = List(4) {
            when(it) {
                0 -> MessagePatch(unread = true)
                1 -> MessagePatch(archive = true)
                2 -> MessagePatch(folderId = 21)
                else -> MessagePatch(note =  "note")
            }
        }


        every { repository.updateMessage(any(), any()) } throws Exception()

        patches.forEach {
            val latch = CountDownLatch(1)

            interactor.input = UpdateMessageInteractor.Input(arrayListOf(mockk()) , it)
            interactor.output = object : UpdateMessageInteractor.Output {
                override fun onUpdateMessageSuccess() {
                    assert(false)
                    latch.countDown()
                }

                override fun onUpdateMessageError(error: ViewError) {
                    assert(true)
                    latch.countDown()
                }

            }

            interactor.run()
            latch.await()
        }


    }

}