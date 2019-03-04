package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class SaveAttachmentInteractorImplTest {

    private val executor = TestExecutor()
    private val permissionManager = mockk<PermissionManager>()
    private val cacheManager = mockk<FileCacheManager>()

    private val interactor = SaveAttachmentInteractorImpl(executor, cacheManager, permissionManager)


    @Test
    fun `Save Attachment Test`() {
        val latch = CountDownLatch(1)
        val content = Content("id", "title", 123L,
                "pdf", "application/pdf", "UTF-8", null)
        val filename = "filename"
        val message = mockk<Message>()

        every { cacheManager.getCachedContentFileName(any()) } returns filename
        every { permissionManager.requestPermission(any()) } returns true
        every { cacheManager.isExternalStorageWritable() } returns true
        every { cacheManager.copyContentToExternalStorage(any()) } returns filename


        interactor.input = SaveAttachmentInteractor.Input(message, content)
        interactor.output = object : SaveAttachmentInteractor.Output {
            override fun onSaveAttachment(filename: String) {
                assert(true)
                latch.countDown()
            }

            override fun onSaveAttachmentError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }

    @Test
    fun `Save Attachment Without Permission Error Test`() {
        val latch = CountDownLatch(1)
        val content = Content("id", "title", 123L,
                "pdf", "application/pdf", "UTF-8", null)
        val filename = "filename"
        val message = mockk<Message>()

        every { cacheManager.getCachedContentFileName(any()) } returns filename

        // Permission is missing
        every { permissionManager.requestPermission(any()) } returns false
        every { cacheManager.isExternalStorageWritable() } returns true
        every { cacheManager.copyContentToExternalStorage(any()) } returns filename


        interactor.input = SaveAttachmentInteractor.Input(message, content)
        interactor.output = object : SaveAttachmentInteractor.Output {
            override fun onSaveAttachment(filename: String) {
                assert(false)
                latch.countDown()
            }

            override fun onSaveAttachmentError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }
    @Test
    fun `Save Attachment That's not exists Error`() {
        val latch = CountDownLatch(1)
        val content = Content("id", "title", 123L,
                "pdf", "application/pdf", "UTF-8", null)
        val filename = "filename"
        val message = mockk<Message>()

        //  File is missing
        every { cacheManager.getCachedContentFileName(any()) } returns null
        every { permissionManager.requestPermission(any()) } returns true
        every { cacheManager.isExternalStorageWritable() } returns true
        every { cacheManager.copyContentToExternalStorage(any()) } returns filename


        interactor.input = SaveAttachmentInteractor.Input(message, content)
        interactor.output = object : SaveAttachmentInteractor.Output {
            override fun onSaveAttachment(filename: String) {
                assert(false)
                latch.countDown()
            }

            override fun onSaveAttachmentError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }
}