package dk.eboks.app.presentation.ui.channels.components.content.storebox.detail

import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelContentStoreboxDetailComponentContract {
    interface View : BaseView {
        fun getReceiptId(): String?
        fun setReceipt(receipt: StoreboxReceipt)
        fun showProgress(isLoading: Boolean)
        fun returnToMasterView()
    }

    interface Presenter : BasePresenter<View> {
        fun loadReceipt()
        fun deleteReceipt()
        fun saveReceipt(dstFolder: Folder)
    }
}