package dk.eboks.app.mail.mail

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.mail.presentation.ui.components.maillist.MailListComponentContract
import dk.eboks.app.mail.presentation.ui.components.maillist.MailListComponentPresenter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MailListComponentPresenterTest {

    private val getMessagesInteractor = mockk<GetMessagesInteractor>(relaxUnitFun = true)
    private val deleteMessagesInteractor = mockk<DeleteMessagesInteractor>(relaxed = true)
    private val moveMessagesInteractor = mockk<MoveMessagesInteractor>(relaxUnitFun = true)
    private val updateMessageInteractor = mockk<UpdateMessageInteractor>(relaxUnitFun = true)

    private val mockView = mockk<MailListComponentContract.View>(relaxUnitFun = true)
    private val mockLifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    private lateinit var presenter: MailListComponentPresenter

    @Before
    fun setUp() {
        presenter = MailListComponentPresenter(
            getMessagesInteractor,
            deleteMessagesInteractor,
            moveMessagesInteractor,
            updateMessageInteractor
        )
        presenter.onViewCreated(mockView, mockLifecycle)
    }

    @Test
    fun `Test Setup With Folder`() {
        val folder = mockk<Folder>()
        presenter.setup(folder)

        verify {
            getMessagesInteractor.input = GetMessagesInteractor.Input(
                false,
                folder,
                null,
                presenter.currentOffset,
                presenter.currentLimit
            )

            presenter.isLoading = true

            mockView.showProgress(true)
        }
    }

    @Test
    fun `Test Setup With Sender`() {
        val sender = mockk<Sender>()
        presenter.setup(sender)
        verify {
            getMessagesInteractor.input = GetMessagesInteractor.Input(
                false,
                null,
                sender,
                presenter.currentOffset,
                presenter.currentLimit
            )
            getMessagesInteractor.run()

            presenter.isLoading = true

            mockView.showProgress(true)
        }
    }

    @Test
    fun `Test Load Next Page`() {
        presenter.isLoading = false
        presenter.currentOffset = 0
        presenter.totalMessages = 100

        // Page gets loaded
        presenter.loadNextPage()

        // Page Wont be loaded becuase last loading is not finished
        presenter.isLoading = true
        presenter.loadNextPage()

        presenter.isLoading = false
        presenter.currentOffset = 100
        presenter.totalMessages = 100

        // No pages left to load
        presenter.loadNextPage()

        // Loading starts only once
        verify(exactly = 1) {
            getMessagesInteractor.run()
            mockView.showRefreshProgress(true)
        }
    }

    @Test
    fun `Test Refresh`() {
        presenter.refresh()
        verify { getMessagesInteractor.run() }
    }

    @Test
    fun `Delete Messages Test`() {
        val messagesToDelete = mockk<MutableList<Message>>(relaxed = true)
        presenter.deleteMessages(messagesToDelete)

        verify {
            mockView.showProgress(true)
            deleteMessagesInteractor.input =
                DeleteMessagesInteractor.Input(ArrayList(messagesToDelete))
            deleteMessagesInteractor.run()
        }
    }

    @Test
    fun `Move Messages Test`() {
        val messagesToMove = mockk<MutableList<Message>>(relaxed = true)
        val folderId = 12
        presenter.moveMessages(folderId, messagesToMove)

        verify {
            val messageIds = ArrayList(messagesToMove.map { it })
            moveMessagesInteractor.input = MoveMessagesInteractor.Input(folderId, messageIds)
            moveMessagesInteractor.run()
        }
    }

    @Test
    fun `Mark Message A Read Test`() {
        val messages = mockk<MutableList<Message>>(relaxed = true)
        val unread = true
        presenter.markReadMessages(messages, unread)

        verify {
            val patch = MessagePatch(unread)
            mockView.showProgress(true)
            updateMessageInteractor.input =
                UpdateMessageInteractor.Input(ArrayList(messages), patch)
            updateMessageInteractor.run()
        }
    }

    @Test
    fun `Archive Message Test`() {
        val messages = mockk<MutableList<Message>>(relaxed = true)
        presenter.archiveMessages(messages)

        verify {
            val patch = MessagePatch(archive = true)
            mockView.showProgress(true)
            updateMessageInteractor.input =
                UpdateMessageInteractor.Input(ArrayList(messages), patch)
            updateMessageInteractor.run()
        }
    }

    @Test
    fun `Test On Get Messages`() {
        presenter.isLoading = true
        presenter.currentOffset = 0

        val messages = List(30) { mockk<Message>(relaxed = true) }

        presenter.onGetMessages(messages)

        verify {
            mockView.showProgress(false)
            mockView.showRefreshProgress(false)
            mockView.showMessages(messages)
        }
    }

    @Test
    fun `Test On Get Empty Messages`() {
        presenter.isLoading = true
        presenter.currentOffset = 0

        val messages = listOf<Message>()

        presenter.onGetMessages(messages)

        verify {
            mockView.showProgress(false)
            mockView.showRefreshProgress(false)
            mockView.showEmpty(true)
        }
    }

    @Test
    fun `On Get Messages Error Test`() {

        val viewError = ViewError()
        presenter.onGetMessagesError(viewError)

        verify {
            mockView.showErrorDialog(viewError)
            mockView.showProgress(false)
            mockView.showRefreshProgress(false)
            mockView.showEmpty(true)
        }
    }

    @Test
    fun `On Move Message Error`() {
        val viewError = ViewError()

        presenter.onUpdateMessageError(viewError)

        verify {
            mockView.showProgress(false)
            mockView.showErrorDialog(viewError)
        }
    }

    @Test
    fun `On Delete Message Error`() {
        val viewError = ViewError()

        presenter.onDeleteMessagesError(viewError)

        verify {
            mockView.showProgress(false)
            mockView.showErrorDialog(viewError)
        }
    }

    @Test
    fun `On Update Message Error`() {
        val viewError = ViewError()

        presenter.onUpdateMessageError(viewError)

        verify {
            mockView.showProgress(false)
            mockView.showErrorDialog(viewError)
        }
    }
}