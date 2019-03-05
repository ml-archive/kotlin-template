package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.formreply.ReplyForm
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

class SubmitReplyFormInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = SubmitReplyFormInteractorImpl(executor, repository)

    @Test
    fun `Submit Reply Form Test`() {

        val latch = CountDownLatch(1)
        val message = Message("m", "s", Date(), false)
        val form = ReplyForm()

        every { repository.submitMessageReplyForm(any(), any()) } returns Unit

        interactor.input = SubmitReplyFormInteractor.Input(message, form)
        interactor.output = object : SubmitReplyFormInteractor.Output {
            override fun onSubmitReplyForm() {
                assert(true)
                latch.countDown()
            }

            override fun onSubmitReplyFormError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Submit Reply Form Error Test`() {

        val latch = CountDownLatch(1)
        val message = Message("m", "s", Date(), false)
        val form = ReplyForm()

        every { repository.submitMessageReplyForm(any(), any()) } throws Exception()

        interactor.input = SubmitReplyFormInteractor.Input(message, form)
        interactor.output = object : SubmitReplyFormInteractor.Output {
            override fun onSubmitReplyForm() {
                assert(false)
                latch.countDown()
            }

            override fun onSubmitReplyFormError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

}