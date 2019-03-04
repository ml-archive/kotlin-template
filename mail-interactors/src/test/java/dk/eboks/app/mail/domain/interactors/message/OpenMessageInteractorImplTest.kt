package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.APIConstants
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ErrorType
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.*
import java.util.concurrent.CountDownLatch

class OpenMessageInteractorImplTest {

    private val executor = TestExecutor()
    private val uiManager = mockk<UIManager>()
    private val appStateManager = mockk<AppStateManager>()
    private val downloadManager = mockk<DownloadManager>()
    private val cacheManager = mockk<FileCacheManager>()
    private val messageRepository = mockk<MessagesRepository>()
    private val appConfig = mockk<AppConfig>()

    private val interactor = OpenMessageInteractorImpl(
            executor, appStateManager, uiManager,
            downloadManager, cacheManager, messageRepository, appConfig
    )

    @Test
    fun `Open Cached Message Test`() {

        val latch = CountDownLatch(1)

        val content = Content("id", "title", 123L,
                "pdf", "application/pdf", "UTF-8", null)
        val message = Message("ider", "s", Date(), false, content = content)
        val messageFilename = "filename"
        val path = "path/to/$messageFilename"
        val contentPath = "path/to/content"

        every { messageRepository.getMessage(any(), any(),any(), any()) } returns message
        every { cacheManager.getCachedContentFileName(any()) } returns messageFilename
        every { cacheManager.getAbsolutePath(any()) } returns path
        every { cacheManager.cacheContent(any(), any()) }  returns Unit
        every { downloadManager.downloadContent(any(), any()) } returns contentPath

        every { uiManager.showEmbeddedMessageScreen() } returns Unit

        every { appStateManager.state } returns AppState()


        interactor.input = OpenMessageInteractor.Input(message)
        interactor.output = object : OpenMessageInteractor.Output {
            override fun onOpenMessageDone() {
                assert(true)
                latch.countDown()
            }

            override fun onOpenMessageServerError(serverError: ServerError) {
                assert(false)
                latch.countDown()
            }

            override fun onOpenMessageError(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onReAuthenticate(loginProviderId: String, msg: Message) {
                assert(false)
                latch.countDown()
            }

            override fun onPrivateSenderWarning(msg: Message) {
                assert(false)
                latch.countDown()
            }

            override fun isViewAttached(): Boolean {
                return true
            }
        }

        interactor.run()
        latch.await()

    }

    @Test
    fun `Open Locked Message`() {

        val latch = CountDownLatch(2)
        val status = Status(type = APIConstants.MSG_LOCKED_REQUIRES_NEW_AUTH)
        val content = Content("id", "title", 123L,
                "pdf", "application/pdf", "UTF-8", null)
        val message = Message("ider", "s", Date(), false, content = content, lockStatus = status)
        val messageFilename = "filename"
        val path = "path/to/$messageFilename"
        val contentPath = "path/to/content"

        every { messageRepository.getMessage(any(), any(),any(), any()) } returns message

        // No content saved
        every { cacheManager.getCachedContentFileName(any()) } returns null
        every { cacheManager.getAbsolutePath(any()) } returns path
        every { cacheManager.cacheContent(any(), any()) }  returns Unit
        every { downloadManager.downloadContent(any(), any()) } returns contentPath

        every { uiManager.showEmbeddedMessageScreen() } returns Unit

        every { appStateManager.state } returns AppState()


        interactor.input = OpenMessageInteractor.Input(message)
        interactor.output = object : OpenMessageInteractor.Output {
            override fun onOpenMessageDone() {
                // Open message when everything finished
                assert(latch.count == 1L)
                latch.countDown()
            }

            override fun onOpenMessageServerError(serverError: ServerError) {
                assert(false)
                latch.countDown()
            }

            override fun onOpenMessageError(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onReAuthenticate(loginProviderId: String, msg: Message) {
                // First ask for re auth
                assert(latch.count == 2L)
                latch.countDown()
            }

            override fun onPrivateSenderWarning(msg: Message) {
                assert(false)
                latch.countDown()
            }

            override fun isViewAttached(): Boolean {
                return true
            }
        }

        interactor.run()
        latch.await()

    }


    @Test
    fun `Download and Open Message Test`() {

        val latch = CountDownLatch(1)

        val content = Content("id", "title", 123L,
                "pdf", "application/pdf", "UTF-8", null)
        val message = Message("ider", "s", Date(), false, content = content)
        val messageFilename = "filename"
        val path = "path/to/$messageFilename"
        val contentPath = "path/to/content"

        every { messageRepository.getMessage(any(), any(),any(), any()) } returns message

        // No content saved
        every { cacheManager.getCachedContentFileName(any()) } returns null
        every { cacheManager.getAbsolutePath(any()) } returns path
        every { cacheManager.cacheContent(any(), any()) }  returns Unit
        every { downloadManager.downloadContent(any(), any()) } returns contentPath

        every { uiManager.showEmbeddedMessageScreen() } returns Unit

        every { appStateManager.state } returns AppState()


        interactor.input = OpenMessageInteractor.Input(message)
        interactor.output = object : OpenMessageInteractor.Output {
            override fun onOpenMessageDone() {
                assert(true)
                latch.countDown()
            }

            override fun onOpenMessageServerError(serverError: ServerError) {
                assert(false)
                latch.countDown()
            }

            override fun onOpenMessageError(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onReAuthenticate(loginProviderId: String, msg: Message) {
                assert(false)
                latch.countDown()
            }

            override fun onPrivateSenderWarning(msg: Message) {
                assert(false)
                latch.countDown()
            }

            override fun isViewAttached(): Boolean {
                return true
            }
        }

        interactor.run()
        latch.await()

    }

}