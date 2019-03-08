package dk.eboks.app.mail.message.viewers

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.mail.presentation.ui.message.components.viewers.html.HtmlViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.html.HtmlViewComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.viewers.image.ImageViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.image.ImageViewComponentPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ImageViewComponentPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val view :  ImageViewComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle : Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: ImageViewComponentPresenter


    @Before
    fun setUp() {
        presenter = ImageViewComponentPresenter(appStateManager)
        presenter.onViewCreated(view, lifecycle)
    }


    @Test
    fun `Setup With Uri String Test`() {
        val uriString = "content://myuri"
        presenter.setup(uriString)

        verify {
            view.showImageURI(uriString)
        }
    }

    @Test
    fun `Setup With Filename Test`() {
        val filename = "filename"
        every { appStateManager.state } returns AppState(currentViewerFileName = filename)

        presenter.setup(null)

        verify {
            view.showImage(filename)
        }
    }
}