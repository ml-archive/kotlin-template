package dk.eboks.app.mail.message

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.mail.domain.interactors.message.SubmitReplyFormInteractor
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormContract
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormPresenter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.Date

class ReplyFormPresenterTest {

    private val getReplyFormInteractor: GetReplyFormInteractor = mockk(relaxUnitFun = true)
    private val submitReplyFormInteractor: SubmitReplyFormInteractor = mockk(relaxUnitFun = true)

    private val view: ReplyFormContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private val message =
        Message("id", folderId = 12, subject = "s", received = Date(), unread = false)

    private lateinit var presenter: ReplyFormPresenter

    @Before
    fun setUp() {
        presenter = ReplyFormPresenter(getReplyFormInteractor, submitReplyFormInteractor)
        presenter.onViewCreated(view, lifecycle)
    }

    @Test
    fun `Setup Test`() {
        presenter.setup(message)

        verify {
            getReplyFormInteractor.input = GetReplyFormInteractor.Input(message.id, 0)
            getReplyFormInteractor.run()
            view.clearForm()
        }
    }

    @Test
    fun `Submit Test`() {

        presenter.currentForm = ReplyForm()
        presenter.currentMessage = message

        presenter.submit()

        presenter.currentMessage = null
        presenter.currentForm = null

        presenter.submit()

        // Submit interactor runs only once
        verify(exactly = 1) {
            submitReplyFormInteractor.run()
        }
    }

    @Test
    fun `On Get Reply Form Test`() {

        val form = ReplyForm()
        presenter.onGetReplyForm(form)

        verify {
            view.showProgress(false)
            form.inputs.forEach { view.showFormInput(input = it) }
        }

        assert(form == presenter.currentForm)
    }

    @Test
    fun `On Get Reply Form Error`() {
        val error = ViewError()
        presenter.onGetReplyFormError(error)

        verify {
            view.showProgress(false)
            view.showErrorDialog(error)
        }
    }
}