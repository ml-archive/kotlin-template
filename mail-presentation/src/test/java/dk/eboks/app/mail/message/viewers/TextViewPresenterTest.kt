package dk.eboks.app.mail.message.viewers

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.mail.presentation.ui.message.components.viewers.text.TextViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.text.TextViewComponentPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TextViewPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val view: TextViewComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: TextViewComponentPresenter

    @Before
    fun setUp() {
        presenter = TextViewComponentPresenter(appStateManager)
        presenter.onViewCreated(view, lifecycle)
    }

    @Test
    fun `Setup With Uri String Test`() {
        val uriString = "content://myuri"
        presenter.setup(uriString)

        verify {
            view.showTextURI(uriString)
        }
    }

    @Test
    fun `Setup With Filename Test`() {
        val filename = "filename"
        every { appStateManager.state } returns AppState(currentViewerFileName = filename)

        presenter.setup(null)

        verify {
            view.showText(filename)
        }
    }
}