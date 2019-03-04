package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch

class DeleteFolderInteractorTestImpl {

    private val executor = TestExecutor()
    private val repository = mockk<FoldersRepository>()
    private val interactor = DeleteFolderInteractorImpl(executor, repository)


    @Test
    fun `Create Folder Test`() {
        val latch = CountDownLatch(1)
        every { repository.deleteFolder(any()) } returns Unit

        interactor.input = DeleteFolderInteractor.Input(1)
        interactor.output = object : DeleteFolderInteractor.Output {

            override fun onDeleteFolderSuccess() {
                assertTrue(true)
                latch.countDown()
            }

            override fun onDeleteFolderError(error: ViewError) {
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
        every { repository.deleteFolder(any()) } throws Exception()

        interactor.input = DeleteFolderInteractor.Input(1)
        interactor.output = object : DeleteFolderInteractor.Output {

            override fun onDeleteFolderSuccess() {
                assertTrue(false)
                latch.countDown()
            }

            override fun onDeleteFolderError(error: ViewError) {
                assertTrue(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

}