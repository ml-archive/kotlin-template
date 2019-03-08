package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class OpenAttachmentInteractorImplTest {

    private val executor = TestExecutor()

    private val downloadManager = mockk<DownloadManager>()
    private val cacheManager = mockk<FileCacheManager>()

    private val interactor = OpenAttachmentInteractorImpl(executor, downloadManager, cacheManager)

    @Test
    fun `Open Cached Attachment Test`() {

        val latch = CountDownLatch(1)
        val content = Content(
            "id", "title", 123L,
            "pdf", "application/pdf", "UTF-8", null
        )
        val message = mockk<Message>()
        val fileName = "filename"
        val path = "path"

        // Attachment is saved in cache
        every { cacheManager.getCachedContentFileName(content) } returns fileName
        every { cacheManager.getAbsolutePath(fileName) } returns path

        interactor.input = OpenAttachmentInteractor.Input(message, content)
        interactor.output = object : OpenAttachmentInteractor.Output {

            override fun onOpenAttachment(attachment: Content, filename: String, mimeType: String) {
                assert(true)
                latch.countDown()
            }

            override fun onOpenAttachmentError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Download and Open Attachment Test`() {

        val latch = CountDownLatch(1)
        val content = Content(
            "id", "title", 123L,
            "pdf", "application/pdf", "UTF-8", null
        )
        val message = mockk<Message>()
        val fileName = "filename"
        val path = "path/to/$fileName"

        // Attachment is not saved in cache
        every { cacheManager.getCachedContentFileName(content) } returns null

        // Download and cache attachment
        every { downloadManager.downloadAttachmentContent(message, content) } returns fileName
        every { cacheManager.cacheContent(fileName, content) } returns Unit

        every { cacheManager.getAbsolutePath(fileName) } returns path

        interactor.input = OpenAttachmentInteractor.Input(message, content)
        interactor.output = object : OpenAttachmentInteractor.Output {

            override fun onOpenAttachment(attachment: Content, filename: String, mimeType: String) {
                assert(true)
                latch.countDown()
            }

            override fun onOpenAttachmentError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Open Attachment Error Test`() {

        val latch = CountDownLatch(1)
        val content = Content(
            "id", "title", 123L,
            "pdf", "application/pdf", "UTF-8", null
        )
        val message = mockk<Message>()
        val fileName = "filename"
        val path = "path/to/$fileName"

        // Attachment is not saved in cache
        every { cacheManager.getCachedContentFileName(content) } returns null

        // error While downloading
        every { downloadManager.downloadAttachmentContent(message, content) } returns null
        every { cacheManager.getAbsolutePath(fileName) } returns path

        interactor.input = OpenAttachmentInteractor.Input(message, content)
        interactor.output = object : OpenAttachmentInteractor.Output {

            override fun onOpenAttachment(attachment: Content, filename: String, mimeType: String) {
                assert(false)
                latch.countDown()
            }

            override fun onOpenAttachmentError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}