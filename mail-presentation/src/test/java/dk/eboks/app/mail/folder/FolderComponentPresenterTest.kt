package dk.eboks.app.mail.folder

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.mail.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.mail.presentation.ui.folder.components.FoldersComponentContract
import dk.eboks.app.mail.presentation.ui.folder.components.FoldersComponentPresenter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FolderComponentPresenterTest {

    private val appStaManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val getFoldersInteractor = mockk<GetFoldersInteractor>(relaxUnitFun = true)
    private val openFoldersInteractor = mockk<OpenFolderInteractor>(relaxUnitFun = true)

    private val mockView = mockk<FoldersComponentContract.View>(relaxUnitFun = true)
    private val mockLifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    private lateinit var presenter: FoldersComponentPresenter

    @Before
    fun setUp() {
        presenter =
            FoldersComponentPresenter(appStaManager, getFoldersInteractor, openFoldersInteractor)
        presenter.onViewCreated(mockView, mockLifecycle)
    }

    @Test
    fun `Open Folder Test`() {
        val folder = mockk<Folder>()
        presenter.openFolder(folder)

        verify {
            openFoldersInteractor.input = OpenFolderInteractor.Input(folder)
            openFoldersInteractor.run()
        }
    }

    @Test
    fun `On Get Folders Test`() {
        val folders = listOf(Folder())
        presenter.onGetFolders(folders)

        verify {
            mockView.showProgress(false)
            mockView.showRefreshProgress(false)
            mockView.showUserFolders(folders)
        }
    }

    @Test
    fun `On Get Folders Error est`() {
        val error = ViewError()
        presenter.onGetFoldersError(error)

        verify {
            mockView.showProgress(false)
            mockView.showRefreshProgress(false)
            mockView.showErrorDialog(error)
        }
    }

    @Test
    fun `On Open Folder Error Test`() {
        val error = ViewError()
        presenter.onOpenFolderError(error)

        verify {
            mockView.showErrorDialog(error)
        }
    }
}