package dk.eboks.app.presentation.ui.channels.components.content.storebox.content

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.local.ViewError
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChannelContentStoreboxComponentPresenterTest {

    private lateinit var presenter: ChannelContentStoreboxComponentPresenter
    private val getStoreboxReceiptsInteractor =
        mockk<GetStoreboxReceiptsInteractor>(relaxUnitFun = true)
    private val getStoreboxCreditCardsInteractor =
        mockk<GetStoreboxCreditCardsInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<ChannelContentStoreboxComponentContract.View>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)
    @Before
    fun setUp() {
        presenter = ChannelContentStoreboxComponentPresenter(
            getStoreboxReceiptsInteractor,
            getStoreboxCreditCardsInteractor
        )
        presenter.onViewCreated(viewMock, lifecycle)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test onViewCreated`() {
        verify { presenter.loadReceipts() }
    }

    @Test
    fun `Test load receipts`() {
        presenter.loadReceipts()
        verify { getStoreboxReceiptsInteractor.run() }
    }

    @Test
    fun `Test on get receipts empty`() {
        presenter.onGetReceipts(listOf())
        verify { getStoreboxCreditCardsInteractor.run() }
    }

    @Test
    fun `Test on get receipts not empty`() {
        val messages = listOf(StoreboxReceiptItem())
        presenter.onGetReceipts(messages)
        verify { viewMock.setReceipts(messages) }
    }

    @Test
    fun `Test on get receipts error`() {
        val viewError = ViewError()
        presenter.onGetReceiptsError(viewError)
        verify { viewMock.showErrorDialog(viewError) }
    }

    @Test
    fun `Test on get cards successful empty`() {
        presenter.onGetCardsSuccessful(listOf())
        verify { viewMock.showNoCreditCardsEmptyView(true) }
    }

    @Test
    fun `Test on get cards successful not empty`() {
        presenter.onGetCardsSuccessful(listOf(StoreboxCreditCard()))
        verify { viewMock.showEmptyView(true) }
    }

    @Test
    fun `Test on get cards error`() {
        val viewError = ViewError()
        presenter.onGetCardsError(viewError)
        verify {
            viewMock.showEmptyView(true)
            viewMock.showErrorDialog(viewError)
        }
    }
}