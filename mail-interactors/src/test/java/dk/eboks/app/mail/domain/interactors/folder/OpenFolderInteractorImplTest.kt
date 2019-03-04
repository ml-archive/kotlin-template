package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class OpenFolderInteractorImplTest {


    private val executor = TestExecutor()
    private val uiManager = mockk<UIManager>()
    private val interactor = OpenFolderInteractorImpl(executor, uiManager)



    @Test
    fun `Open Folder Test`() {
        val latch = CountDownLatch(1)
        every { uiManager.showFolderContentScreen(any()) } returns Unit

        interactor.input = OpenFolderInteractor.Input(Folder(1))
        interactor.output = object : OpenFolderInteractor.Output {
            override fun onOpenFolderDone() {
                Assert.assertTrue(true)
                latch.countDown()
            }

            override fun onOpenFolderError(error: ViewError) {
                Assert.assertTrue(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Open Folder Test Error`() {
        val latch = CountDownLatch(1)
        every { uiManager.showFolderContentScreen(any()) } throws Exception()

        interactor.input = OpenFolderInteractor.Input(Folder(1))
        interactor.output = object : OpenFolderInteractor.Output {
            override fun onOpenFolderDone() {
                Assert.assertTrue(false)
                latch.countDown()
            }

            override fun onOpenFolderError(error: ViewError) {
                Assert.assertTrue(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

}