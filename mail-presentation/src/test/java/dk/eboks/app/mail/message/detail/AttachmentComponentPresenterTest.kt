package dk.eboks.app.mail.message.detail

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.mail.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.mail.presentation.ui.message.components.detail.attachments.AttachmentsComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.attachments.AttachmentsComponentPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AttachmentComponentPresenterTest {

    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val openAttachmentInteractor = mockk<OpenAttachmentInteractor>(relaxUnitFun = true)
    private val saveAttachmentInteractor = mockk<SaveAttachmentInteractor>(relaxUnitFun = true)

    private val mockView = mockk<AttachmentsComponentContract.View>(relaxUnitFun = true)
    private val mockLifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    private lateinit var presenter: AttachmentsComponentPresenter
    private val message = mockk<Message>(relaxed = true)

    @Before
    fun setUp() {
        every { appStateManager.state } returns AppState(currentMessage = message)
        presenter = AttachmentsComponentPresenter(
            appStateManager,
            openAttachmentInteractor,
            saveAttachmentInteractor
        )
        presenter.onViewCreated(mockView, mockLifecycle)
    }

    @Test
    fun `Test Open Attachment`() {
        val content = mockk<Content>(relaxed = true)
        presenter.openAttachment(content)
        verify {
            openAttachmentInteractor.input = OpenAttachmentInteractor.Input(message, content)
            openAttachmentInteractor.run()
        }
    }

    @Test
    fun `Test Save Attachment`() {
        val content = mockk<Content>(relaxed = true)
        presenter.saveAttachment(content)
        verify {
            saveAttachmentInteractor.input = SaveAttachmentInteractor.Input(message, content)
            saveAttachmentInteractor.run()
        }
    }

    @Test
    fun `Test On Attahcment Open`() {
        val content = mockk<Content>()
        val name = "name"
        val mimetype = "*/*"

        presenter.onOpenAttachment(content, name, mimetype)
        verify {
            mockView.openExternalViewer(content, name, mimetype)
        }
    }
}