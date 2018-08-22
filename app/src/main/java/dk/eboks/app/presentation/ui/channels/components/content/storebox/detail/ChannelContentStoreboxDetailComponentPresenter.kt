package dk.eboks.app.presentation.ui.channels.components.content.storebox.detail

import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxDetailComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        private val getStoreboxReceiptInteractor: GetStoreboxReceiptInteractor,
        private val deleteStoreboxReceiptInteractor: DeleteStoreboxReceiptInteractor,
        private val saveReceiptInteractor: SaveReceiptInteractor
) :
        ChannelContentStoreboxDetailComponentContract.Presenter,
        BasePresenterImpl<ChannelContentStoreboxDetailComponentContract.View>(),
        GetStoreboxReceiptInteractor.Output,
        DeleteStoreboxReceiptInteractor.Output,
        SaveReceiptInteractor.Output
{

    var currentReceipt : StoreboxReceipt? = null
    var currentFolderName : String? = null

    init {
        getStoreboxReceiptInteractor.output = this
        deleteStoreboxReceiptInteractor.output = this
        saveReceiptInteractor.output = this
    }

    override fun loadReceipt() {
        Timber.d("loadReceipt")
        val storeboxId = view?.getReceiptId() ?: return
        getStoreboxReceiptInteractor.input = GetStoreboxReceiptInteractor.Input(storeboxId)
        getStoreboxReceiptInteractor.run()
    }

    override fun deleteReceipt() {
        currentReceipt?.let {
            runAction { v->v.showProgress(true) }
            deleteStoreboxReceiptInteractor.input = DeleteStoreboxReceiptInteractor.Input(it.id)
            deleteStoreboxReceiptInteractor.run()
        }
    }

    override fun saveReceipt(dstFolder: Folder) {
        currentReceipt?.let { receipt ->
            currentFolderName = dstFolder.name
            runAction { v->v.showProgress(true) }
            saveReceiptInteractor.input = SaveReceiptInteractor.Input(receipt.id, dstFolder.id)
            saveReceiptInteractor.run()
        }
    }

    /**
     * GetReceiptInteractor callbacks
     */

    override fun onGetReceipt(storeboxReceipt: StoreboxReceipt) {
        currentReceipt = storeboxReceipt
        runAction { v->
            v.setReceipt(storeboxReceipt)
        }

    }

    override fun onGetReceiptsError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    /**
     * DeleteStoreboxReceiptInteractor callbacks
     */

    override fun onDeleteReceiptSuccess() {
        runAction { v->
            v.returnToMasterView()
        }
    }

    override fun onDeleteReceiptError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    /**
     * SaveReceiptInteractor callbacks
     */

    override fun onSaveReceiptSuccess() {
        runAction { v ->
            v.showProgress(false)
            currentFolderName?.let { name->
                v.showToast(Translation.storeboxreceipt.receiptSavedToFolderToast.replace("[folderName]", name))
            }
        }
    }

    override fun onSaveReceiptError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}