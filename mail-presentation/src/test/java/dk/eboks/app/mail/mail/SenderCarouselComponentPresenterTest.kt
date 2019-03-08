package dk.eboks.app.mail.mail

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractor
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.SenderCarouselComponentPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SenderCarouselComponentPresenterTest {


    private val appStateManager = mockk<AppStateManager>(relaxUnitFun = true)
    private val getSendersInteractor = mockk<GetSendersInteractor>(relaxUnitFun = true)

    private val mockView = mockk<SenderCarouselComponentContract.View>(relaxUnitFun = true)
    private val mockLifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    private lateinit var presenter : SenderCarouselComponentPresenter


    @Before
    fun setUp() {
        presenter = SenderCarouselComponentPresenter(appStateManager, getSendersInteractor)
        presenter.onViewCreated(mockView, mockLifecycle)
    }


    @Test
    fun `Test On Get Senders`() {

        every { appStateManager.state?.currentUser?.verified } returns true

        val senders = List(21) {
            Sender(it.toLong(), it.toString(), unreadMessageCount = it)
        }.shuffled()

        presenter.onGetSenders(senders)

        verify {
            mockView.showProgress(false)
            mockView.showEmpty(false, true)
            mockView.showSenders(presenter.sortSenders(senders))
        }


    }


    @Test
    fun `Test On Get Empty Senders`() {

        every { appStateManager.state?.currentUser?.verified } returns true

        val senders = listOf<Sender>()

        presenter.onGetSenders(senders)

        verify {
            mockView.showProgress(false)
            mockView.showEmpty(true, true)
        }

    }

    @Test
    fun `Test On Get Senders Error`() {
        val error = ViewError()
        presenter.onGetSendersError(error)

        verify {
            mockView.showProgress(false)
            mockView.showErrorDialog(error)
        }

    }

}