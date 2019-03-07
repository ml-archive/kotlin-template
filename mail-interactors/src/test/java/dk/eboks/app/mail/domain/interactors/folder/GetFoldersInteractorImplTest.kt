package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderMode
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.folder.isSystemFolder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class GetFoldersInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<FoldersRepository>()
    private val interactor = GetFoldersInteractorImpl(executor, repository)

    // Preset folders with different types
    private val mockFolders = List(30) { index ->
        val type = when (index) {
            in 0..5 -> FolderType.FOLDER
            in 5..10 -> FolderType.INBOX
            in 10..15 -> FolderType.ARCHIVE
            in 15..20 -> FolderType.UPLOADS
            else -> FolderType.DRAFTS
        }
        Folder(type = type)
    }

    @Test
    fun `Test Get Folders SELECT`() {
        every { repository.getFolders(any(), any()) } returns mockFolders
        val latch = CountDownLatch(1)

        // Running interactor in select mode
        interactor.input = GetFoldersInteractor.Input(false, FolderMode.SELECT, 12)
        interactor.output = object : GetFoldersInteractor.Output {

            // Result folders should consist only be of type INBOX or FOLDER
            override fun onGetFolders(folders: List<Folder>) {
                folders.map { it.type == FolderType.INBOX || it.type == FolderType.FOLDER }
                        .forEach { Assert.assertTrue(it) }
                latch.countDown()
            }

            override fun onGetFoldersError(error: ViewError) {
                Assert.assertTrue(false)
                latch.countDown()
            }

            // Shouldn't be called when in SELECT mode
            override fun onGetSystemFolders(folders: List<Folder>) {
                Assert.assertTrue(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Get Folders NORMAL`() {
        every { repository.getFolders(any(), any()) } returns mockFolders
        val latch = CountDownLatch(2)

        // Running interactor in NORMAL mode
        interactor.input = GetFoldersInteractor.Input(false, FolderMode.NORMAL, userId = 12)
        interactor.output = object : GetFoldersInteractor.Output {

            // Should only consist of folders with FOLDER type
            override fun onGetFolders(folders: List<Folder>) {
                Assert.assertTrue(!folders.any { it.type != FolderType.FOLDER })
                latch.countDown()
            }

            // Should only consists of system folders
            override fun onGetSystemFolders(folders: List<Folder>) {
                Assert.assertTrue(!folders.any { it.type?.isSystemFolder() == false })
                latch.countDown()
            }

            override fun onGetFoldersError(error: ViewError) {
                Assert.assertTrue(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Get Folders NORMAL Error`() {
        every { repository.getFolders(any(), any()) } throws Exception()
        val latch = CountDownLatch(1)

        // Running interactor in NORMAL mode
        interactor.input = GetFoldersInteractor.Input(false, FolderMode.NORMAL, userId = 12)
        interactor.output = object : GetFoldersInteractor.Output {

            // Should only consist of folders with FOLDER type
            override fun onGetFolders(folders: List<Folder>) {
                Assert.assertTrue(false)
                latch.countDown()
            }

            // Should only consists of system folders
            override fun onGetSystemFolders(folders: List<Folder>) {
                Assert.assertTrue(false)
                latch.countDown()
            }

            override fun onGetFoldersError(error: ViewError) {
                Assert.assertTrue(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Get Folders SELECT Error`() {
        every { repository.getFolders(any(), any()) } throws Exception()
        val latch = CountDownLatch(1)

        // Running interactor in SELECT mode
        interactor.input = GetFoldersInteractor.Input(false, FolderMode.SELECT, userId = 12)
        interactor.output = object : GetFoldersInteractor.Output {

            // Should only consist of folders with FOLDER type
            override fun onGetFolders(folders: List<Folder>) {
                Assert.assertTrue(false)
                latch.countDown()
            }

            // Should only consists of system folders
            override fun onGetSystemFolders(folders: List<Folder>) {
                Assert.assertTrue(false)
                latch.countDown()
            }

            override fun onGetFoldersError(error: ViewError) {
                Assert.assertTrue(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}