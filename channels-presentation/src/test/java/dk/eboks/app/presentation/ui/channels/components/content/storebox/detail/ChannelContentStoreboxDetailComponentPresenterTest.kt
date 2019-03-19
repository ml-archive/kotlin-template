package dk.eboks.app.presentation.ui.channels.components.content.storebox.detail

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractor
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class ChannelContentStoreboxDetailComponentPresenterTest {

    private lateinit var presenter: ChannelContentStoreboxDetailComponentPresenter
    private val getStoreboxReceiptInteractor =
        mockk<GetStoreboxReceiptInteractor>(relaxUnitFun = true)
    private val deleteStoreboxReceiptInteractor =
        mockk<DeleteStoreboxReceiptInteractor>(relaxUnitFun = true)
    private val saveReceiptInteractor = mockk<SaveReceiptInteractor>(relaxUnitFun = true)
    private val shareReceiptInteractor = mockk<ShareReceiptInteractor>(relaxUnitFun = true)
    private val viewMock =
        mockk<ChannelContentStoreboxDetailComponentContract.View>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = ChannelContentStoreboxDetailComponentPresenter(
            getStoreboxReceiptInteractor,
            deleteStoreboxReceiptInteractor,
            saveReceiptInteractor,
            shareReceiptInteractor
        )
        presenter.onViewCreated(viewMock, lifecycle)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test load receipt`() {
        val receiptId = "1610"
        presenter.loadReceipt(receiptId)
        verify {
            getStoreboxReceiptInteractor.input = GetStoreboxReceiptInteractor.Input(receiptId)
            getStoreboxReceiptInteractor.run()
        }
    }

    @Test
    fun `Test delete receipt`() {
        presenter.currentReceipt = StoreboxReceipt("12345")
        presenter.deleteReceipt()
        verify {
            viewMock.showProgress(true)
            deleteStoreboxReceiptInteractor.input = DeleteStoreboxReceiptInteractor.Input("12345")
            deleteStoreboxReceiptInteractor.run()
        }
    }

    @Test
    fun `Test save receipt`() {
        val dstFolder = Folder(1538, name = "name")
        presenter.currentReceipt = StoreboxReceipt("12345")
        presenter.saveReceipt(dstFolder)
        verify {
            presenter.currentFolderName = "name"
            viewMock.showProgress(true)
            saveReceiptInteractor.input = SaveReceiptInteractor.Input("12345", 1538)
            saveReceiptInteractor.run()
        }
    }

    @Test
    fun `Test share receipt`() {
        val bool = Random.nextBoolean()
        presenter.currentReceipt = StoreboxReceipt("12345")
        presenter.shareReceipt(bool)
        verify {
            presenter.shareAsMail = bool
            viewMock.showProgress(true)
            shareReceiptInteractor.input = ShareReceiptInteractor.Input("12345")
            shareReceiptInteractor.run()
        }
    }

    @Test
    fun `Test on get receipt`() {
        val storeboxReceipt = StoreboxReceipt("12356")
        presenter.onGetReceipt(storeboxReceipt)
        verify {
            presenter.currentReceipt = storeboxReceipt
            viewMock.setReceipt(storeboxReceipt)
        }
    }

    @Test
    fun `Test on get receipts error`() {
        val viewError = ViewError()
        presenter.onGetReceiptsError(viewError)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(viewError)
        }
    }

    @Test
    fun `Test on delete receipt success`() {
        presenter.onDeleteReceiptSuccess()
        verify {
            viewMock.returnToMasterView()
        }
    }

    @Test
    fun `Test on delete receipt error`() {
        val error = ViewError()
        presenter.onDeleteReceiptError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on save receipt success`() {
        val name = "folderName"
        presenter.currentFolderName = name
        presenter.onSaveReceiptSuccess()
        verify {
            viewMock.showProgress(false)
            viewMock.showToast(
                Translation.storeboxreceipt.receiptSavedToFolderToast.replace(
                    "[folderName]",
                    name
                )
            )
        }
    }

    @Test
    fun `Test on save receipt error`() {
        val error = ViewError()
        presenter.onSaveReceiptError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on share receipt success`() {
        val name = "fileName"
        presenter.shareAsMail = true
        presenter.onShareReceiptSuccess(name)
        verify {
            viewMock.showProgress(false)
            viewMock.mailReceiptContent(name)
        }
    }

    @Test
    fun `Test on share receipt success not mail`() {
        val name = "fileName"
        presenter.shareAsMail = false
        presenter.onShareReceiptSuccess(name)
        verify {
            viewMock.showProgress(false)
            viewMock.shareReceiptContent(name)
        }
    }

    @Test
    fun `Test on share receipt error`() {
        val error = ViewError()
        presenter.onShareReceiptError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }
}