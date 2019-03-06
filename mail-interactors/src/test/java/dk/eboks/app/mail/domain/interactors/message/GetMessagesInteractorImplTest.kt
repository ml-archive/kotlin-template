package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.HashMap

class GetMessagesInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = GetMessagesInteractorImpl(executor, repository)

    private fun initPair(type: FolderType) : Pair<FolderType, List<Message>> {
        return type to List(type.ordinal) {
            index -> Message(index.toString(), "$type $index", Date(), true)
        }
    }

    @Test
    fun `Get Messages From Folder Test`() {

        val folderMessages = List(15) { index -> Message(index.toString(), "Subject $index", Date(), false) }


        val latch = CountDownLatch(1)
        every { repository.getMessagesByFolder(any(), any(), any()) } returns folderMessages
        every { repository.getMessagesBySender(any(), any(), any()) } returns listOf()
        every { repository.getLatest(any()) } returns listOf()
        every { repository.getUnread(any()) } returns listOf()
        every { repository.getUploads(any()) } returns listOf()
        every { repository.getHighlights(any()) } returns listOf()

        // Get Messages from Folder
        interactor.input = GetMessagesInteractor.Input(false, Folder(1))
        interactor.output = object : GetMessagesInteractor.Output {
            override fun onGetMessages(messages: List<Message>) {
                assert(true)
                assert(messages.containsAll(folderMessages))
                assert(messages.size == folderMessages.size)
                latch.countDown()
            }

            override fun onGetMessagesError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }


    @Test
    fun `Get Messages From Sender Test`() {

        val senderMessages = List(15) { index -> Message(index.toString(), "Subject $index", Date(), false) }


        val latch = CountDownLatch(1)
        every { repository.getMessagesByFolder(any(), any(), any()) } returns listOf()
        every { repository.getMessagesBySender(any(), any(), any()) } returns senderMessages
        every { repository.getLatest(any()) } returns listOf()
        every { repository.getUnread(any()) } returns listOf()
        every { repository.getUploads(any()) } returns listOf()
        every { repository.getHighlights(any()) } returns listOf()

        // Get Messages from Folder
        interactor.input = GetMessagesInteractor.Input(false, sender = Sender(1))
        interactor.output = object : GetMessagesInteractor.Output {
            override fun onGetMessages(messages: List<Message>) {
                assert(true)
                assert(messages.containsAll(senderMessages))
                assert(messages.size == senderMessages.size)
                latch.countDown()
            }

            override fun onGetMessagesError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }


    @Test
    fun `Get Messages From System Folder Test`() {

        val messages :  HashMap<FolderType, List<Message>> = hashMapOf(
                initPair(FolderType.HIGHLIGHTS),
                initPair(FolderType.UPLOADS),
                initPair(FolderType.LATEST),
                initPair(FolderType.UNREAD)
        )


        every { repository.getMessagesByFolder(any(), any(), any()) } returns listOf()
        every { repository.getMessagesBySender(any(), any(), any()) } returns listOf()

        // Setup system messages
        every { repository.getLatest(any()) } returns messages.getOrDefault(FolderType.LATEST, listOf())
        every { repository.getUnread(any()) } returns messages.getOrDefault(FolderType.UNREAD, listOf())
        every { repository.getUploads(any()) } returns messages.getOrDefault(FolderType.UPLOADS, listOf())
        every { repository.getHighlights(any()) } returns messages.getOrDefault(FolderType.HIGHLIGHTS, listOf())

        // Get Messages from Folder

        messages.forEach { folderType, mockedMessages ->

            val latch = CountDownLatch(1)


            interactor.input = GetMessagesInteractor.Input(false, Folder(0, type = folderType))
            interactor.output = object : GetMessagesInteractor.Output {

                override fun onGetMessages(messages: List<Message>) {
                    // checking that resulting messages are the same type as requested
                    assert(messages.containsAll(mockedMessages))
                    latch.countDown()
                }

                override fun onGetMessagesError(error: ViewError) {
                    assert(false)
                    latch.countDown()
                }
            }
            interactor.run()
            latch.await()
        }


    }

    @Test
    fun `Get Messages Error Test`() {
        every { repository.getMessagesByFolder(any(), any(), any()) } throws Exception()
        every { repository.getMessagesBySender(any(), any(), any()) } throws Exception()
        every { repository.getLatest(any()) } throws Exception()
        every { repository.getUnread(any()) } throws Exception()
        every { repository.getUploads(any()) } throws Exception()
        every { repository.getHighlights(any()) } throws Exception()

        val inputs = List(4) {index ->
            when(index) {
                // Folder request
                0 -> GetMessagesInteractor.Input(false, Folder(1))

                // sender request
                1 -> GetMessagesInteractor.Input(false, sender = Sender(1))

                // system folder request
                2 -> GetMessagesInteractor.Input(false, Folder(0))

                // Request with the null params
                else -> GetMessagesInteractor.Input(false, null, null)
            }
        }

        inputs.forEach {
            val latch = CountDownLatch(1)
            interactor.input = it
            interactor.output = object : GetMessagesInteractor.Output {
                override fun onGetMessages(messages: List<Message>) {
                    assert(false)
                    latch.countDown()
                }

                override fun onGetMessagesError(error: ViewError) {
                    assert(true)
                    latch.countDown()
                }
            }

            interactor.run()
            latch.await()
        }

    }



}