package dk.eboks.app.senders.presentation.ui.components.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class RegisterPresenterTest {

    private lateinit var presenter: RegisterPresenter
    private val registerInteractor = mockk<RegisterInteractor>(relaxUnitFun = true)
    private val unRegisterInteractor = mockk<UnRegisterInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<RegistrationContract.View>(relaxUnitFun = true)
    @Before
    fun setUp() {
        presenter = RegisterPresenter(
            registerInteractor,
            unRegisterInteractor
        )
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test register sender`() {
        val sender = Sender(Random.nextLong())
        presenter.registerSender(sender)
        verify {
            registerInteractor.inputSender = RegisterInteractor.InputSender(sender.id)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test register sender group`() {
        val senderId = Random.nextLong()
        val senderGroup = mockk<SenderGroup>()
        presenter.registerSenderGroup(senderId, senderGroup)
        verify {
            registerInteractor.inputSenderGroup =
                RegisterInteractor.InputSenderGroup(senderId, senderGroup)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test register segment`() {
        val segmentId = Random.nextLong()
        val segment = mockk<Segment>()
        every { segment.id } returns segmentId
        presenter.registerSegment(segment)
        verify {
            registerInteractor.inputSegment = RegisterInteractor.InputSegment(segmentId)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test unregister sender`() {
        val sender = Sender(Random.nextLong())
        presenter.unregisterSender(sender)
        verify {
            unRegisterInteractor.inputSender = UnRegisterInteractor.InputSender(sender.id)
            unRegisterInteractor.run()
        }
    }

    @Test
    fun `Test unregister sender group`() {
        val senderId = Random.nextLong()
        val senderGroup = mockk<SenderGroup>()
        presenter.unregisterSenderGroup(senderId, senderGroup)
        verify {
            unRegisterInteractor.inputSenderGroup =
                UnRegisterInteractor.InputSenderGroup(senderId, senderGroup)
            unRegisterInteractor.run()
        }
    }

    @Test
    fun `Test unregister segment`() {
        val segmentId = Random.nextLong()
        val segment = mockk<Segment>()
        every { segment.id } returns segmentId
        presenter.unregisterSegment(segment)
        verify {
            unRegisterInteractor.inputSegment = UnRegisterInteractor.InputSegment(segmentId)
            unRegisterInteractor.run()
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
    fun `Test on error`() {
        val error = ViewError()
        presenter.onError(error)
        verify {
            viewMock.showError(error.message ?: "")
        }
    }
}