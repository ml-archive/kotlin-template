package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch

class CreateFolderInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<FoldersRepository>()
    private val interactor = CreateFolderInteractorImpl(executor, repository)


    @Test
    fun `Create Folder Test`() {
        val latch = CountDownLatch(1)
        val folderRequest = FolderRequest(12, 21, "New Folder")
        every { repository.createFolder(any()) } returns Unit

        interactor.input = CreateFolderInteractor.Input(folderRequest)
        interactor.output = object : CreateFolderInteractor.Output {
            override fun onCreateFolderSuccess() {
                assertTrue(true)
                latch.countDown()
            }

            override fun onCreateFolderError(error: ViewError) {
                assertTrue(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Create Folder Error Test`() {
        val latch = CountDownLatch(1)
        val folderRequest = FolderRequest(12, 21, "New Folder")
        every { repository.createFolder(any()) } throws Exception()

        interactor.input = CreateFolderInteractor.Input(folderRequest)
        interactor.output = object : CreateFolderInteractor.Output {
            override fun onCreateFolderSuccess() {
                assertTrue(false)
                latch.countDown()
            }

            override fun onCreateFolderError(error: ViewError) {
                assertTrue(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

}