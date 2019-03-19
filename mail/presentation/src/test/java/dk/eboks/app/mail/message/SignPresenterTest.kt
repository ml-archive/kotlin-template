package dk.eboks.app.mail.message

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.mail.domain.interactors.message.GetSignLinkInteractor
import dk.eboks.app.mail.presentation.ui.message.screens.sign.SignContract
import dk.eboks.app.mail.presentation.ui.message.screens.sign.SignPresenter
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SignPresenterTest {

    private val getSignLinkInteractor: GetSignLinkInteractor = mockk(relaxUnitFun = true)
    private val view: SignContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: SignPresenter

    @Before
    fun setUp() {
        presenter = SignPresenter(getSignLinkInteractor)
        presenter.onViewCreated(view, lifecycle)
    }

    @Test
    fun `Test Setup`() {
        val message = mockk<Message>(relaxed = true)
        presenter.setup(message)

        verify {
            getSignLinkInteractor.input = GetSignLinkInteractor.Input(message)
            getSignLinkInteractor.run()
        }
    }

    @Test
    fun `On Get Sign Link`() {
        val link = Link()
        presenter.onGetSignLink(link)

        verify {
            view.loadUrl(link.url)
        }
    }

    @Test
    fun `On Get Sign Link Error`() {
        val error = ViewError()
        presenter.onGetSignLinkError(error)

        verify {
            view.showErrorDialog(error)
        }
    }
}