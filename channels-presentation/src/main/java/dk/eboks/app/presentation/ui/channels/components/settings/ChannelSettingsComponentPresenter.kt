package dk.eboks.app.presentation.ui.channels.components.settings

import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractor
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelFlags
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.ViewController
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ChannelSettingsComponentPresenter @Inject constructor(
    private val storeboxCreditCardsInteractor: GetStoreboxCreditCardsInteractor,
    private val deleteStoreboxCreditCardInteractor: DeleteStoreboxCreditCardInteractor,
    private val getStoreboxProfileInteractor: GetStoreboxProfileInteractor,
    private val putStoreboxProfileInteractor: PutStoreboxProfileInteractor,
    private val getStoreboxCardLinkInteractor: GetStoreboxCardLinkInteractor,
    private val deleteStoreboxAccountLinkInteractor: DeleteStoreboxAccountLinkInteractor,
    private val updateStoreboxFlagsInteractor: UpdateStoreboxFlagsInteractor,
    private val getChannelInteractor: GetChannelInteractor,
    private val uninstallChannelInteractor: UninstallChannelInteractor,
    private val viewController: ViewController
) : ChannelSettingsComponentContract.Presenter,
    BasePresenterImpl<ChannelSettingsComponentContract.View>(),
    DeleteStoreboxCreditCardInteractor.Output,
    GetStoreboxCreditCardsInteractor.Output,
    GetStoreboxProfileInteractor.Output,
    PutStoreboxProfileInteractor.Output,
    GetStoreboxCardLinkInteractor.Output,
    DeleteStoreboxAccountLinkInteractor.Output,
    UpdateStoreboxFlagsInteractor.Output,
    GetChannelInteractor.Output,
    UninstallChannelInteractor.Output {

    override var currentChannel: Channel? = null

    init {
        getStoreboxProfileInteractor.output = this
        putStoreboxProfileInteractor.output = this
        storeboxCreditCardsInteractor.output = this
        deleteStoreboxCreditCardInteractor.output = this
        getStoreboxProfileInteractor.output = this
        getStoreboxCardLinkInteractor.output = this
        deleteStoreboxAccountLinkInteractor.output = this
        updateStoreboxFlagsInteractor.output = this
        getChannelInteractor.output = this
        uninstallChannelInteractor.output = this
    }

    override fun setup(channelId: Int) {
        refreshChannel(channelId)
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

    override fun saveStoreboxProfile(profile: StoreboxProfile) {
        putStoreboxProfileInteractor.input = PutStoreboxProfileInteractor.Input(profile)
        putStoreboxProfileInteractor.run()
    }

    override fun getStoreboxCardLink() {
        view { showProgress(true) }
        getStoreboxCardLinkInteractor.run()
    }

    override fun deleteStoreboxAccountLink() {
        view { showProgress(true) }
        deleteStoreboxAccountLinkInteractor.run()
    }

    override fun updateChannelFlags(channel: Channel, flags: ChannelFlags) {
        updateStoreboxFlagsInteractor.input = UpdateStoreboxFlagsInteractor.Input(channel.id, flags)
        updateStoreboxFlagsInteractor.run()
    }

    override fun removeChannel() {
        uninstallChannelInteractor.input = UninstallChannelInteractor.Input(currentChannel?.id ?: return)
        view { showProgress(true) }
        uninstallChannelInteractor.run()
    }

    private fun refreshChannel(channelId: Int) {
        getChannelInteractor.input = GetChannelInteractor.Input(channelId)
        getChannelInteractor.run()
    }

    /**
     * Interactor callbacks ----------------------------------------------------------------------->
     */

    override fun onGetCardsSuccessful(result: List<StoreboxCreditCard>) {
        view {
            showProgress(false)
            setCreditCards(result)
        }
    }

    override fun onGetCardsError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onDeleteCardSuccess(result: Boolean) {
        getCreditCards()
    }

    override fun onDeleteCardError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onGetProfile(result: StoreboxProfile) {
        view { setOnlyDigitalReceipts(result.greenProfile) }
        getCreditCards()
    }

    override fun onGetProfileError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onPutProfile() {
        Timber.d("Storebox profile saved")
        view { showProgress(false) }
    }

    override fun onPutProfileError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onGetStoreboxCardLink(result: Link) {
        view { showAddCardView(result) }
    }

    override fun onGetStoreboxCardLinkError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onStoreboxAccountLinkDelete() {
        viewController.refreshChannelComponent = true
        view {
            showProgress(false)
            broadcastCloseChannel()
            closeView()
        }
    }

    override fun onStoreboxAccountLinkDeleteError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onUpdateFlagsSuccess() {
        viewController.refreshChannelComponent = true
    }

    override fun onUpdateFlagsError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    /**
     * GetChannelInteractor callbacks
     */

    override fun onGetChannel(channel: Channel) {
        currentChannel = channel
        view { setupChannel(channel) }
    }

    override fun onGetChannelError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    /**
     * UninstallChannelInteractor callbacks
     */

    override fun onUninstallChannel() {
        viewController.refreshChannelComponent = true
        view {
            showProgress(false)
            broadcastCloseChannel()
            closeView()
        }
    }

    override fun onUninstallChannelError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }
}