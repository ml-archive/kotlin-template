package dk.eboks.app.mail.mail

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.mail.domain.interactors.categories.GetCategoriesInteractor
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.FolderShortcutsComponentPresenter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FolderShortcutsComponentPresenterTest {

    private val getCategoriesInteractor = mockk<GetCategoriesInteractor>(relaxUnitFun = true)
    private val openFolderInteractor = mockk<OpenFolderInteractor>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)
    private val view = mockk<FolderShortcutsComponentContract.View>(relaxUnitFun = true)

    private lateinit var presenter: FolderShortcutsComponentPresenter

    @Before
    fun setUp() {
        presenter = FolderShortcutsComponentPresenter(getCategoriesInteractor, openFolderInteractor)
        presenter.onViewCreated(view, lifecycle)
    }

    @Test
    fun `Test Open Folder`() {
        val folder = mockk<Folder>()
        presenter.openFolder(folder)
        verify {
            openFolderInteractor.input = OpenFolderInteractor.Input(folder)
            openFolderInteractor.run()
        }
    }

    @Test
    fun `Test On Get Categories`() {
        val folders = mockk<List<Folder>>(relaxed = true)
        presenter.onGetCategories(folders)
        verify {
            view.showProgress(false)
            view.showFolders(folders)
        }
    }

    @Test
    fun `Test On Get Categories Error`() {
        val error = ViewError()
        presenter.onGetCategoriesError(error)
        verify {
            view.showErrorDialog(error)
            view.showProgress(false)
        }
    }
}