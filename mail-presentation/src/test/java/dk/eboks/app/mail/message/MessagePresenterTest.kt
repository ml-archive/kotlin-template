package dk.eboks.app.mail.message

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.*
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.mail.presentation.ui.message.screens.MessageContract
import dk.eboks.app.mail.presentation.ui.message.screens.MessagePresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MessagePresenterTest {
    private val appConfig: AppConfig = mockk(relaxUnitFun = true, relaxed = true)
    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val deleteMessagesInteractor: DeleteMessagesInteractor = mockk(relaxUnitFun = true)
    private val updateMessageInteractor: UpdateMessageInteractor = mockk(relaxUnitFun = true)

    private val view: MessageContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: MessagePresenter


    private val replyStatus: Status = mockk(relaxed = true)
    private val payment: Payment = mockk(relaxed = true)
    private val attachments: List<Content> = listOf()
    private val sign = mockk<Sign>(relaxed = true)

    private val message: Message = mockk(relaxed = true) {
        every { reply } returns this@MessagePresenterTest.replyStatus
        every { attachments } returns this@MessagePresenterTest.attachments
        every { payment } returns this@MessagePresenterTest.payment
        every { sign } returns this@MessagePresenterTest.sign
    }

    @Before
    fun setUp() {

        every { appStateManager.state } returns AppState(currentMessage = message)

        presenter = MessagePresenter(appStateManager, appConfig, deleteMessagesInteractor, updateMessageInteractor)
        presenter.onViewCreated(view, lifecycle)

    }


    @Test
    fun `Setup Test`() {

        // All Disabled
        every { appConfig.isReplyEnabled } returns false
        every { appConfig.isDocumentActionsEnabled } returns false
        every { appConfig.isSignEnabled } returns false

        presenter.setup()

        // All Enabled
        every { appConfig.isReplyEnabled } returns true
        every { appConfig.isDocumentActionsEnabled } returns true
        every { appConfig.isSignEnabled } returns true

        presenter.setup()

        //
        verify(exactly = 2) {
            view.addHeaderComponentFragment()
            view.addNotesComponentFragment()
            view.addShareComponentFragment()
            view.addFolderInfoComponentFragment()
            view.showTitle(message)
            view.addAttachmentsComponentFragment()

        }

        // Replies, action etc actions called only once
        verify(exactly = 1) {
            view.addReplyButtonComponentFragment(message)
        }
    }


    @Test
    fun `Test Move Message`() {

        presenter.setup()

        val folder = Folder()
        presenter.moveMessage(folder)

        verify {
            val patch = MessagePatch(folderId = folder.id)
            updateMessageInteractor.input = UpdateMessageInteractor.Input(arrayListOf(message), patch)
            updateMessageInteractor.run()
        }
    }

    @Test
    fun `Test Delete Move Message`() {

        presenter.setup()
        presenter.deleteMessage()

        verify {
            deleteMessagesInteractor.input = DeleteMessagesInteractor.Input(arrayListOf(message))
            deleteMessagesInteractor.run()
        }
    }


    @Test
    fun `Test Archive Message`() {

        presenter.setup()
        presenter.archiveMessage()

        verify {
            val patch = MessagePatch(archive = true)
            updateMessageInteractor.input = UpdateMessageInteractor.Input(arrayListOf(message), patch)
            updateMessageInteractor.run()
        }
    }

    @Test
    fun `Test Read Message`() {

        presenter.setup()
        presenter.markAsUnread(false)

        verify {
            val patch = MessagePatch(unread = false)
            updateMessageInteractor.input = UpdateMessageInteractor.Input(arrayListOf(message), patch)
            updateMessageInteractor.run()
        }


    }

    @Test
    fun `Test Unread Message`() {

        presenter.setup()
        presenter.markAsUnread(true)

        verify {
            val patch = MessagePatch(unread = true)
            updateMessageInteractor.input = UpdateMessageInteractor.Input(arrayListOf(message), patch)
            updateMessageInteractor.run()
        }
    }


    @Test
    fun `Test On Update Message Error`() {
        val error = ViewError()
        presenter.onUpdateMessageError(error)

        verify {
            view.showErrorDialog(error)
        }
    }

}