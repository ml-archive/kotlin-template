package dk.eboks.app.senders.presentation.ui.components.list

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
import kotlin.random.Random

class SenderAllListComponentPresenterTest {

    private lateinit var presenter: SenderAllListComponentPresenter
    private val viewMock = mockk<SenderAllListComponentContract.View>(relaxUnitFun = true)
    private val appState = mockk<AppStateManager>(relaxUnitFun = true)
    private val getSendersInteractor = mockk<GetSendersInteractor>(relaxUnitFun = true)
    private val userId = Random.nextInt()
    @Before
    fun setUp() {
        every { appState.state }.returns(AppState(impersoniateUser = SharedUser(userId = userId)))
        presenter = SenderAllListComponentPresenter(appState, getSendersInteractor)
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test refresh`() {
        presenter.refresh()
        verify {
            getSendersInteractor.input = GetSendersInteractor.Input(userId = userId)
            getSendersInteractor.output = presenter
            getSendersInteractor.run()
        }
    }

    @Test
    fun `Test on get senders`() {
        val senders = listOf(
            Sender(Random.nextLong()),
            Sender(Random.nextLong()),
            Sender(Random.nextLong()),
            Sender(Random.nextLong())
        )
        presenter.onGetSenders(senders)
        verify {
            presenter.senders.clear()
            presenter.filteredSenders.clear()
            presenter.senders.addAll(senders)
            presenter.filteredSenders.addAll(presenter.senders)
            viewMock.run {
                showProgress(false)
                showEmpty(false)
                showSenders(presenter.filteredSenders)
            }
        }
        // Test with empty list
        presenter.onGetSenders(emptyList())
        verify {
            viewMock.run {
                showProgress(false)
                showEmpty(true)
            }
        }
    }

    @Test
    fun `Test on get senders error`() {
        val error = ViewError()
        presenter.onGetSendersError(error)
        verify {
            viewMock.run {
                showProgress(false)
                showErrorDialog(error)
            }
        }
    }

    @Test
    fun `Test load all senders`() {
        presenter.senders.clear()
        presenter.senders.addAll(
            listOf(
                Sender(Random.nextLong()),
                Sender(Random.nextLong())
            )
        )
        presenter.loadAllSenders()
        verify {
            viewMock.showSenders(presenter.senders)
        }
    }

    @Test
    fun `Test search senders`() {

        val senderMock1 = Sender(Random.nextLong(), name = "tchala")
        val senderMock2 = Sender(Random.nextLong(), name = "Ahala")

        val senders = listOf(
            senderMock2,
            senderMock1,
            Sender(Random.nextLong()),
            Sender(Random.nextLong())
        )
        presenter.senders += senders
        presenter.searchSenders("ala")
        verify {
            viewMock.showSenders(listOf(senderMock2, senderMock1))
        }
    }
}