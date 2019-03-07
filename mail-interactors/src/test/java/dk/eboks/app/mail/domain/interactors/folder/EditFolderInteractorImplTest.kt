package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch

class EditFolderInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<FoldersRepository>()
    private val interactor = EditFolderInteractorImpl(executor, repository)

    @Test
    fun `Edit Folder Test`() {
        val latch = CountDownLatch(1)
        every { repository.editFolder(any(), any()) } returns Unit

        interactor.input = EditFolderInteractor.Input(1, "folderName", 2)
        interactor.output = object : EditFolderInteractor.Output {
            override fun onEditFolderSuccess() {
                Assert.assertTrue(true)
                latch.countDown()
            }

            override fun onEditFolderError(error: ViewError) {
                Assert.assertTrue(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Edit Folder Error Test`() {
        val latch = CountDownLatch(1)
        every { repository.editFolder(any(), any()) } throws Exception()

        interactor.input = EditFolderInteractor.Input(1, "folderName", 12)
        interactor.output = object : EditFolderInteractor.Output {

            override fun onEditFolderSuccess() {
                Assert.assertTrue(false)
                latch.countDown()
            }

            override fun onEditFolderError(error: ViewError) {
                Assert.assertTrue(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}