package dk.eboks.app.mail.message.viewers

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf.PdfViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf.PdfViewComponentPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class PdfViewPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val view: PdfViewComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: PdfViewComponentPresenter

    @Test
    fun `View CreatedTest`() {

        val filename = "filename"
        every { appStateManager.state } returns AppState(currentViewerFileName = filename)

        presenter = PdfViewComponentPresenter(appStateManager)
        presenter.onViewCreated(view, lifecycle)

        verify {
            view.showPdfView(filename)
        }
    }
}