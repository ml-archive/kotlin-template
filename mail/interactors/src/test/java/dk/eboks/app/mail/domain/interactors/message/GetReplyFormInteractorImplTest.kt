package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetReplyFormInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = GetReplyFormInteractorImpl(executor, repository)

    @Test
    fun `Get Reply Form Test`() {

        val latch = CountDownLatch(1)
        val replyForm = ReplyForm()

        every { repository.getMessageReplyForm(1, "id") } returns replyForm

        interactor.input = GetReplyFormInteractor.Input("id", 1)
        interactor.output = object : GetReplyFormInteractor.Output {
            override fun onGetReplyForm(form: ReplyForm) {
                assert(true)
                assert(form == replyForm)
                latch.countDown()
            }

            override fun onGetReplyFormError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Reply Form Error Test`() {
        val latch = CountDownLatch(1)

        every { repository.getMessageReplyForm(1, "id") } throws Exception()

        interactor.input = GetReplyFormInteractor.Input("id", 1)
        interactor.output = object : GetReplyFormInteractor.Output {
            override fun onGetReplyForm(form: ReplyForm) {
                assert(false)
                latch.countDown()
            }

            override fun onGetReplyFormError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}