package dk.eboks.app.presentation.ui.components.channels.settings

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelSettingsComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val storeboxCreditCardsInteractor: GetStoreboxCreditCardsInteractor,
        private val deleteStoreboxCreditCardInteractor: DeleteStoreboxCreditCardInteractor,
        private val getStoreboxProfileInteractor: GetStoreboxProfileInteractor,
        private val putStoreboxProfileInteractor: PutStoreboxProfileInteractor,
        private val getStoreboxCardLinkInteractor: GetStoreboxCardLinkInteractor
) : ChannelSettingsComponentContract.Presenter,
    BasePresenterImpl<ChannelSettingsComponentContract.View>(),
    DeleteStoreboxCreditCardInteractor.Output,
    GetStoreboxCreditCardsInteractor.Output,
    GetStoreboxProfileInteractor.Output,
    PutStoreboxProfileInteractor.Output,
    GetStoreboxCardLinkInteractor.Output
{

    init {
        getStoreboxProfileInteractor.output = this
        putStoreboxProfileInteractor.output = this
        storeboxCreditCardsInteractor.output = this
        deleteStoreboxCreditCardInteractor.output = this
        getStoreboxProfileInteractor.output = this
        getStoreboxCardLinkInteractor.output = this
    }

    override fun onViewCreated(view: ChannelSettingsComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        getCreditCards()
    }

    override fun getCreditCards() {
        storeboxCreditCardsInteractor.run()
    }

    override fun deleteCreditCard(id: String) {
        deleteStoreboxCreditCardInteractor.input = DeleteStoreboxCreditCardInteractor.Input(id)
        deleteStoreboxCreditCardInteractor.run()
    }

    override fun getStoreboxProfile() {
        getStoreboxProfileInteractor.run()
    }

    override fun saveStoreboxProfile(profile : StoreboxProfile) {
        putStoreboxProfileInteractor.input = PutStoreboxProfileInteractor.Input(profile)
        putStoreboxProfileInteractor.run()
    }

    override fun getStoreboxCardLink() {
        runAction { it.showProgress(true) }
        getStoreboxCardLinkInteractor.run()
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

    override fun onGetProfile(result: StoreboxProfile) {
        runAction { v -> v.setOnlyDigitalReceipts(result.greenProfile) }
    }

    override fun onGetProfileError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }

    override fun onPutProfile() {
        Timber.d("Storebox profile saved")
    }

    override fun onPutProfileError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }

    override fun onGetStoreboxCardLink(result: Link) {
        runAction {
            it.showAddCardView(result)
        }
    }

    override fun onGetStoreboxCardLinkError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}