package dk.eboks.app.presentation.ui.components.channels.settings

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelSettingsComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val storeboxCreditCardsInteractor: GetStoreboxCreditCardsInteractor,
        private val deleteStoreboxCreditCardInteractor: DeleteStoreboxCreditCardInteractor
) : ChannelSettingsComponentContract.Presenter,
    BasePresenterImpl<ChannelSettingsComponentContract.View>(),
    DeleteStoreboxCreditCardInteractor.Output,
    GetStoreboxCreditCardsInteractor.Output {

    override fun onViewCreated(view: ChannelSettingsComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        getCreditCards()
    }

    override fun getCreditCards() {
        storeboxCreditCardsInteractor.output = this
        storeboxCreditCardsInteractor.run()
    }

    override fun deleteCreditCard(id: String) {
        deleteStoreboxCreditCardInteractor.input = DeleteStoreboxCreditCardInteractor.Input(id)
        deleteStoreboxCreditCardInteractor.output = this
        deleteStoreboxCreditCardInteractor.run()
    }

    override fun onGetCardsSuccessful(result: MutableList<StoreboxCreditCard>) {
        runAction {
            it.setCreditCards(result)
        }
    }

    override fun onGetCardsError(error: ViewError) {
        runAction {
            it.showProgress(false)
            it.showErrorDialog(error)
        }
    }

    override fun onDeleteCardSuccess(result: Boolean) {
        getCreditCards()
    }

    override fun onDeleteCardError(error: ViewError) {
        runAction {
            it.showProgress(false)
            it.showErrorDialog(error)
        }
    }

}