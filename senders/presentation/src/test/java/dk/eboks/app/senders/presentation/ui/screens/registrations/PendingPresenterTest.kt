package dk.eboks.app.senders.presentation.ui.screens.registrations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.senders.interactors.register.GetPendingInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class PendingPresenterTest {

    private lateinit var presenter: PendingPresenter
    private val getPendingInteractor = mockk<GetPendingInteractor>(relaxUnitFun = true)
    private val registerInteractor = mockk<RegisterInteractor>(relaxUnitFun = true)
    private val unregisterInteractor = mockk<UnRegisterInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<PendingContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = PendingPresenter(
            getPendingInteractor,
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
    fun `Test init`() {
        verify {
            getPendingInteractor.output = presenter
            registerInteractor.output = presenter
            unregisterInteractor.output = presenter
        }
    }

    @Test
    fun `Test load pending`() {
        presenter.loadPending()
        verify {
            getPendingInteractor.run()
        }
    }

    @Test
    fun `Test on registrations loaded`() {
        val list = listOf<CollectionContainer>(mockk(), mockk())
        presenter.onRegistrationsLoaded(list)
        verify {
            viewMock.showPendingRegistrations(list)
        }
    }

    @Test
    fun `Test on error`() {
        val error = ViewError()
        presenter.onError(error)
        verify {
            viewMock.showErrorDialog(error)
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
    fun `Test register segment`() {
        val segmentId = Random.nextLong()
        presenter.registerSegment(segmentId)
        verify {
            registerInteractor.inputSegment = RegisterInteractor.InputSegment(segmentId)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test unregister segment`() {
        val senderId = Random.nextLong()
        presenter.unregisterSegment(senderId)
        verify {
            unregisterInteractor.inputSegment = UnRegisterInteractor.InputSegment(senderId)
            unregisterInteractor.run()
        }
    }

    @Test
    fun `Test on success`() {
        presenter.onSuccess()
        verify {
            viewMock.showRegistrationSuccess()
        }
    }
}