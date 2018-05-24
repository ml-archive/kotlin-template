package dk.eboks.app.presentation.ui.components.channels.content.storebox.detail

import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxDetailComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        private val getStoreboxReceiptInteractor: GetStoreboxReceiptInteractor
) :
        ChannelContentStoreboxDetailComponentContract.Presenter,
        BasePresenterImpl<ChannelContentStoreboxDetailComponentContract.View>(),
        GetStoreboxReceiptInteractor.Output {


    init {
        getStoreboxReceiptInteractor.output = this
    }

    override fun loadReceipt() {
        Timber.d("loadReceipt")
        val storeboxId = view?.getReceiptId() ?: return
        getStoreboxReceiptInteractor.input = GetStoreboxReceiptInteractor.Input(storeboxId)
        getStoreboxReceiptInteractor.run()
    }

    override fun onGetReceipt(storeboxReceipt: StoreboxReceipt) {
        runAction { v->
            v.setReceipt(storeboxReceipt)
        }

    }

    override fun onGetReceiptsError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }

}