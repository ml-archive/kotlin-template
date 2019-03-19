package dk.eboks.app.mail.folder

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.mail.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.EditFolderInteractor
import dk.eboks.app.mail.presentation.ui.folder.components.NewFolderComponentContract
import dk.eboks.app.mail.presentation.ui.folder.components.NewFolderComponentPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class NewFolderPresenterTest {

    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val createFolderInteractor = mockk<CreateFolderInteractor>(relaxUnitFun = true)
    private val deleteFolderInteractor = mockk<DeleteFolderInteractor>(relaxUnitFun = true)
    private val editFolderInteractor = mockk<EditFolderInteractor>(relaxUnitFun = true)

    private val mockView = mockk<NewFolderComponentContract.View>(relaxUnitFun = true)
    private val mockLifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    private lateinit var presenter: NewFolderComponentPresenter

    private val sharedUser = SharedUser(12, 12, name = "Shared User")
    private val currentUser = User(123, "Current USer")

    @Before
    fun setUp() {
        every { appStateManager.state } returns AppState(
            impersoniateUser = sharedUser,
            currentUser = currentUser
        )

        presenter = NewFolderComponentPresenter(
            appStateManager,
            createFolderInteractor,
            deleteFolderInteractor,
            editFolderInteractor
        )
        presenter.onViewCreated(mockView, mockLifecycle)
    }

    @Test
    fun `Test Create New Folder`() {

        val parentFolder = 21
        val name = "new folder name"

        // Shared user is disabled
        every { mockView.overrideActiveUser } returns false

        presenter.createNewFolder(parentFolder, name)

        verify {
            // user id should be null
            createFolderInteractor.input =
                CreateFolderInteractor.Input(FolderRequest(null, parentFolder, name))
            createFolderInteractor.run()
        }
    }

    @Test
    fun `Test Create New Shared Folder`() {

        val parentFolder = 21
        val name = "new folder name"

        // Shared user is enabled
        every { mockView.overrideActiveUser } returns true

        presenter.createNewFolder(parentFolder, name)

        verify {
            // user id should be null
            createFolderInteractor.input =
                CreateFolderInteractor.Input(FolderRequest(sharedUser.userId, parentFolder, name))
            createFolderInteractor.run()
        }
    }

    @Test
    fun `Delete Folder Test`() {
        val folderId = 23
        presenter.deleteFolder(folderId)

        verify {
            deleteFolderInteractor.input = DeleteFolderInteractor.Input(folderId)
            deleteFolderInteractor.run()
        }
    }

    @Test
    fun `Edit Folder Test`() {
        val folderId = 23
        val parentFolderId = 21
        val name = "edited name"
        presenter.editFolder(folderId, parentFolderId, name)

        verify {
            editFolderInteractor.input = EditFolderInteractor.Input(folderId, name, parentFolderId)
            editFolderInteractor.run()
        }
    }

    @Test
    fun `On Create Folder Error`() {
        val error = ViewError()
        presenter.onCreateFolderError(error)

        verify {
            mockView.showErrorDialog(error)
        }
    }

    @Test
    fun `On Edit Folder Error`() {
        val error = ViewError()
        presenter.onEditFolderError(error)

        verify {
            mockView.showErrorDialog(error)
        }
    }

    @Test
    fun `On Delete Folder Error`() {
        val error = ViewError()
        presenter.onDeleteFolderError(error)

        verify {
            mockView.showErrorDialog(error)
        }
    }
}