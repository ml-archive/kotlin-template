package dk.eboks.app.senders.presentation.ui.screens.browse

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class BrowseCategoryPresenterTest {

    private lateinit var presenter: BrowseCategoryPresenter
    private val getSendersInteractor = mockk<GetSendersInteractor>(relaxUnitFun = true)
    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val viewMock = mockk<BrowseCategoryContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)
    private val userId = 10

    @Before
    fun setUp() {
        presenter = BrowseCategoryPresenter(appStateManager, getSendersInteractor)
        presenter.onViewCreated(viewMock, lifecycleMock)
        every { appStateManager.state } returns AppState(impersoniateUser = SharedUser(userId = userId))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test load senders`() {
        val senderId = 123L
        presenter.loadSenders(senderId)
        verify {
            viewMock.showProgress(true)
            getSendersInteractor.input = GetSendersInteractor.Input(
                false,
                "",
                userId,
                senderId
            )
            getSendersInteractor.run()
        }
    }

    @Test
    fun `Test search senders`() {
        val searchText = "sender"
        presenter.searchSenders(searchText)
        verify {
            viewMock.showProgress(true)
            getSendersInteractor.input = GetSendersInteractor.Input(false, searchText, userId)
            getSendersInteractor.run()

        }
    }

    @Test
    fun `Test search senders empty`() {
        val searchText = ""
        presenter.searchSenders(searchText)
        verify {
            viewMock.showProgress(true)
            viewMock.showProgress(false)
            viewMock.showSenders(emptyList())
        }
    }

    @Test
    fun `Test on get senders`() {
        val senders = listOf(Sender(1), Sender(2))
        presenter.onGetSenders(senders)
        verify {
            viewMock.showProgress(false)
            viewMock.showSenders(senders)
        }
    }

    @Test
    fun `Test on get senders error`() {
        val error = ViewError()
        presenter.onGetSendersError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showSenders(emptyList())
            viewMock.showErrorDialog(error)
        }
    }
}