package dk.eboks.app.presentation.ui.components.channels.content

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        private val getStoreboxReceiptsInteractor: GetStoreboxReceiptsInteractor
) :
        ChannelContentStoreboxComponentContract.Presenter,
        BasePresenterImpl<ChannelContentStoreboxComponentContract.View>(),
        GetStoreboxReceiptsInteractor.Output {

    override fun onViewCreated(
            view: ChannelContentStoreboxComponentContract.View,
            lifecycle: Lifecycle
    ) {
        super.onViewCreated(view, lifecycle)
        loadReceipts()
    }

    override fun loadReceipts() {
        Timber.d("loadReceipts")
        getStoreboxReceiptsInteractor.output = this
        getStoreboxReceiptsInteractor.run()
    }

    override fun onGetReceipts(messages: ArrayList<StoreboxReceiptItem>) {
        Timber.d("onGetReceipts: %s", messages.size)
        view?.setReceipts(messages)
    }

    override fun onGetReceiptsError(error: ViewError) {
        Timber.e("onGetReceiptsError: %s", error)
        view?.showErrorDialog(error)
    }

}