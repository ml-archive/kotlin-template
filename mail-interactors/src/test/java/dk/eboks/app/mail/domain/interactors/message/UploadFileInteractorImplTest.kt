package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class UploadFileInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = UploadFileInteractorImpl(executor, repository)

    @Test
    fun `File Upload Test`() {

        val latch = CountDownLatch(1)

        every { repository.uploadFileAsMessage(any(), any(), any(), any(), any()) } returns Unit

        interactor.input = UploadFileInteractor.Input(12, "filename", "uri", "*/*")
        interactor.output = object : UploadFileInteractor.Output {
            override fun onUploadFileComplete() {
                assert(true)
                latch.countDown()
            }

            override fun onUploadFileProgress(pct: Double) {
                assert(true)
            }

            override fun onUploadFileError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }


    @Test
    fun `File Upload Error Test`() {

        val latch = CountDownLatch(1)

        every { repository.uploadFileAsMessage(any(), any(), any(), any(), any()) } throws Exception()

        interactor.input = UploadFileInteractor.Input(12, "filename", "uri", "*/*")
        interactor.output = object : UploadFileInteractor.Output {
            override fun onUploadFileComplete() {
                assert(false)
                latch.countDown()
            }

            override fun onUploadFileProgress(pct: Double) {
                assert(true)
            }

            override fun onUploadFileError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }

}