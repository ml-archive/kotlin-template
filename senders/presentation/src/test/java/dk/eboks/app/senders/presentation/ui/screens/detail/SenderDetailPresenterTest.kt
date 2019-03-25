package dk.eboks.app.senders.presentation.ui.screens.detail

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.senders.interactors.GetSenderDetailInteractor
import dk.eboks.app.domain.senders.interactors.register.GetSenderRegistrationLinkInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class SenderDetailPresenterTest {
    private lateinit var presenter: SenderDetailPresenter
    private val getSenderDetailInteractor = mockk<GetSenderDetailInteractor>(relaxUnitFun = true)
    private val registerInteractor = mockk<RegisterInteractor>(relaxUnitFun = true)
    private val unregisterInteractor = mockk<UnRegisterInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<SenderDetailContract.View>(relaxUnitFun = true)
    private val linkInteractor: GetSenderRegistrationLinkInteractor = mockk(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = SenderDetailPresenter(
                linkInteractor,
                mockk(),
                getSenderDetailInteractor,
                registerInteractor,
                unregisterInteractor
        )
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test load sender`() {
        val senderId = Random.nextLong()
        presenter.loadSender(senderId)
        verify {
            getSenderDetailInteractor.input = GetSenderDetailInteractor.Input(senderId)
            getSenderDetailInteractor.run()
        }
    }

    @Test
    fun `Test register sender`() {
        val senderId = Random.nextLong()
        presenter.registerSender(senderId)
        verify {
            registerInteractor.inputSender = RegisterInteractor.InputSender(senderId)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test unregister sender`() {
        val senderId = Random.nextLong()
        presenter.unregisterSender(senderId)
        verify {
            unregisterInteractor.inputSender = UnRegisterInteractor.InputSender(senderId)
            unregisterInteractor.run()
        }
    }

    @Test
    fun `Test on get sender`() {
        val sender = Sender(Random.nextLong())
        presenter.onGetSender(sender)
        verify {
            viewMock.showSender(sender)
        }
    }

    @Test
    fun `Test on get sender error`() {
        val viewError = ViewError()
        presenter.onGetSenderError(viewError)
        verify {
            viewMock.showErrorDialog(viewError)
        }
    }

    @Test
    fun `Test on success`() {
        presenter.onSuccess()
        verify {
            viewMock.showSuccess()
        }
    }

    @Test
    fun `Register View Link Test`() {
        val id = 1L
        presenter.registerViaLink(id)
        verify {
            linkInteractor.input = GetSenderRegistrationLinkInteractor.Input(id)
            linkInteractor.run()
        }
    }

    @Test
    fun `Test on error`() {
        val viewError = ViewError()
        presenter.onError(viewError)
        verify {
            viewMock.showError(viewError.message ?: "")
        }
    }
}