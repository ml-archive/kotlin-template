package dk.eboks.app.presentation.ui.channels.components.content.storebox.detail

import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractor
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

internal class ChannelContentStoreboxDetailComponentPresenter @Inject constructor(
    private val getStoreboxReceiptInteractor: GetStoreboxReceiptInteractor,
    private val deleteStoreboxReceiptInteractor: DeleteStoreboxReceiptInteractor,
    private val saveReceiptInteractor: SaveReceiptInteractor,
    private val shareReceiptInteractor: ShareReceiptInteractor
) :
    ChannelContentStoreboxDetailComponentContract.Presenter,
    BasePresenterImpl<ChannelContentStoreboxDetailComponentContract.View>(),
    GetStoreboxReceiptInteractor.Output,
    DeleteStoreboxReceiptInteractor.Output,
    SaveReceiptInteractor.Output,
    ShareReceiptInteractor.Output {

    private var currentReceipt: StoreboxReceipt? = null
    private var currentFolderName: String? = null
    private var shareAsMail = false

    init {
        getStoreboxReceiptInteractor.output = this
        deleteStoreboxReceiptInteractor.output = this
        saveReceiptInteractor.output = this
        shareReceiptInteractor.output = this
    }

    override fun loadReceipt() {
        Timber.d("loadReceipt")
        val storeboxId = view?.getReceiptId() ?: return
        getStoreboxReceiptInteractor.input = GetStoreboxReceiptInteractor.Input(storeboxId)
        getStoreboxReceiptInteractor.run()
    }

    override fun deleteReceipt() {
        currentReceipt?.let {
            view { showProgress(true) }
            deleteStoreboxReceiptInteractor.input = DeleteStoreboxReceiptInteractor.Input(it.id)
            deleteStoreboxReceiptInteractor.run()
        }
    }

    override fun saveReceipt(dstFolder: Folder) {
        currentReceipt?.let { receipt ->
            currentFolderName = dstFolder.name
            view { showProgress(true) }
            saveReceiptInteractor.input = SaveReceiptInteractor.Input(receipt.id, dstFolder.id)
            saveReceiptInteractor.run()
        }
    }

    override fun shareReceipt(asMail: Boolean) {
        shareAsMail = asMail
        currentReceipt?.let { receipt ->
            view { showProgress(true) }
            shareReceiptInteractor.input = ShareReceiptInteractor.Input(receipt.id)
            shareReceiptInteractor.run()
        }
    }

    /**
     * GetReceiptInteractor callbacks
     */

    override fun onGetReceipt(storeboxReceipt: StoreboxReceipt) {
        currentReceipt = storeboxReceipt
        view { setReceipt(storeboxReceipt) }
    }

    override fun onGetReceiptsError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    /**
     * DeleteStoreboxReceiptInteractor callbacks
     */

    override fun onDeleteReceiptSuccess() {
        view { returnToMasterView() }
    }

    override fun onDeleteReceiptError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    /**
     * SaveReceiptInteractor callbacks
     */

    override fun onSaveReceiptSuccess() {
        view {
            showProgress(false)
            currentFolderName?.let { name ->
                showToast(
                    Translation.storeboxreceipt.receiptSavedToFolderToast.replace(
                        "[folderName]",
                        name
                    )
                )
            }
        }
    }

    override fun onSaveReceiptError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    /**
     * ShareReceiptInteractor callbacks
     */

    override fun onShareReceiptSuccess(filename: String) {
        view {
            showProgress(false)
            if (!shareAsMail) shareReceiptContent(filename)
            else mailReceiptContent(filename)
        }
        Timber.e("PDF saved to temporary file $filename")
    }

    override fun onShareReceiptError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }
}