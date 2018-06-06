package dk.eboks.app.presentation.ui.components.channels.settings

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.storebox.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.ChannelFlags
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
        private val getStoreboxCardLinkInteractor: GetStoreboxCardLinkInteractor,
        private val deleteStoreboxAccountLinkInteractor: DeleteStoreboxAccountLinkInteractor,
        private val updateStoreboxFlagsInteractor: UpdateStoreboxFlagsInteractor
) : ChannelSettingsComponentContract.Presenter,
    BasePresenterImpl<ChannelSettingsComponentContract.View>(),
    DeleteStoreboxCreditCardInteractor.Output,
    GetStoreboxCreditCardsInteractor.Output,
    GetStoreboxProfileInteractor.Output,
    PutStoreboxProfileInteractor.Output,
    GetStoreboxCardLinkInteractor.Output,
    DeleteStoreboxAccountLinkInteractor.Output,
    UpdateStoreboxFlagsInteractor.Output
{

    init {
        getStoreboxProfileInteractor.output = this
        putStoreboxProfileInteractor.output = this
        storeboxCreditCardsInteractor.output = this
        deleteStoreboxCreditCardInteractor.output = this
        getStoreboxProfileInteractor.output = this
        getStoreboxCardLinkInteractor.output = this
        deleteStoreboxAccountLinkInteractor.output = this
        updateStoreboxFlagsInteractor.output = this
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

    override fun deleteStoreboxAccountLink() {
        runAction { it.showProgress(true) }
        deleteStoreboxAccountLinkInteractor.run()
    }

    override fun updateChannelFlags(flags: ChannelFlags) {
        updateStoreboxFlagsInteractor.input = UpdateStoreboxFlagsInteractor.Input(flags)
        updateStoreboxFlagsInteractor.run()
    }

    /**
     * Interactor callbacks ----------------------------------------------------------------------->
     */

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

    override fun onStoreboxAccountLinkDelete() {
        runAction { v ->
            v.showProgress(false)
            v.broadcastCloseChannel()
            v.closeView()
        }
    }

    override fun onStoreboxAccountLinkDeleteError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onUpdateFlagsSuccess() {

    }

    override fun onUpdateFlagsError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }
}