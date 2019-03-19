package dk.eboks.app.senders.presentation.ui.screens.registrations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Registrations
import dk.eboks.app.domain.senders.interactors.register.GetRegistrationsInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegistrationsPresenterTest {

    private lateinit var presenter: RegistrationsPresenter
    private val registrationsInteractor = mockk<GetRegistrationsInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<RegistrationsContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = RegistrationsPresenter(registrationsInteractor)
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test init`() {
        verify {
            registrationsInteractor.output = presenter
            registrationsInteractor.run()
        }
    }

    @Test
    fun `Test on registrations loaded`() {
        val registrations = mockk<Registrations>()
        presenter.onRegistrationsLoaded(registrations)
        verify {
            viewMock.showRegistrations(registrations)
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
}