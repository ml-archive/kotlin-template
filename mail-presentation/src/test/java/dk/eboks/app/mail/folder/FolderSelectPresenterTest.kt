package dk.eboks.app.mail.folder

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.mail.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.mail.presentation.ui.folder.components.FolderSelectUserComponentContract
import dk.eboks.app.mail.presentation.ui.folder.components.FolderSelectUserComponentPresenter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FolderSelectPresenterTest {

    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val getAllSharesInteractor = mockk<GetAllSharesInteractor>(relaxUnitFun = true)

    private val mockLifecycle = mockk<Lifecycle>(relaxUnitFun = true)
    private val mockView = mockk<FolderSelectUserComponentContract.View>(relaxUnitFun = true)

    private lateinit var presenter: FolderSelectUserComponentPresenter

    @Before
    fun setUp() {
        presenter = FolderSelectUserComponentPresenter(appStateManager, getAllSharesInteractor)
        presenter.onViewCreated(mockView, mockLifecycle)
    }

    @Test
    fun `Get Shares Test`() {
        presenter.getShared()
        verify { getAllSharesInteractor.run() }
    }

    @Test
    fun `On Get Shares Test`() {

        val sharedFolders = List(2) { mockk<SharedUser>() }

        presenter.onGetAllShares(sharedFolders)

        verify {
            mockView.showProgress(false)
            mockView.showShares(sharedFolders)
        }
    }

    @Test
    fun `On Get Shares Error Test`() {

        val error = ViewError()

        presenter.onGetAllSharesError(error)

        verify {
            mockView.showProgress(false)
            mockView.showErrorDialog(error)
        }
    }
}