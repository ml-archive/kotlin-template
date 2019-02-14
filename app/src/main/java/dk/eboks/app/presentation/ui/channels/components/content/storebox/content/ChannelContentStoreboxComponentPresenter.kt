package dk.eboks.app.presentation.ui.channels.components.content.storebox.content

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxComponentPresenter @Inject constructor(
    private val getStoreboxReceiptsInteractor: GetStoreboxReceiptsInteractor,
    private val getStoreboxCreditCardsInteractor: GetStoreboxCreditCardsInteractor
) :
    ChannelContentStoreboxComponentContract.Presenter,
    BasePresenterImpl<ChannelContentStoreboxComponentContract.View>(),
    GetStoreboxReceiptsInteractor.Output,
    GetStoreboxCreditCardsInteractor.Output {

    init {
        getStoreboxReceiptsInteractor.output = this
        getStoreboxCreditCardsInteractor.output = this
    }

    override fun onViewCreated(
        view: ChannelContentStoreboxComponentContract.View,
        lifecycle: Lifecycle
    ) {
        super.onViewCreated(view, lifecycle)
        loadReceipts()
    }

    /**
     * Methods called from the view
     */
    override fun loadReceipts() {
        Timber.d("loadReceipts")
        getStoreboxReceiptsInteractor.run()
    }

    /**
     * Interactor callbacks ------------------------------------------------------------------------
     */
    override fun onGetReceipts(messages: List<StoreboxReceiptItem>) {
        Timber.d("onGetReceipts: %s", messages.size)
        if (messages.isEmpty()) {
            getStoreboxCreditCardsInteractor.run()
        } else {
            runAction { view?.setReceipts(messages) }
        }
    }

    override fun onGetReceiptsError(error: ViewError) {
        Timber.e("onGetReceiptsError: %s", error)
        runAction {
            view?.showErrorDialog(error)
        }
    }

    override fun onGetCardsSuccessful(result: MutableList<StoreboxCreditCard>) {
        // if no credits are added show specialized call to action empty view
        if (result.isEmpty()) {
            runAction { v -> v.showNoCreditCardsEmptyView(true) }
        } else // show regular empty view
        {
            runAction { v -> v.showEmptyView(true) }
        }
    }

    override fun onGetCardsError(error: ViewError) {
        runAction {
            view?.showEmptyView(true)
            view?.showErrorDialog(error)
        }
    }
}