package dk.eboks.app.presentation.ui.components.channels.content.storebox.content

import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelContentStoreboxComponentContract {
    interface View : BaseView {
        fun setReceipts(data: List<StoreboxReceiptItem>)
        fun showProgress(show: Boolean)
        fun showEmptyView(show: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun loadReceipts()
    }
}