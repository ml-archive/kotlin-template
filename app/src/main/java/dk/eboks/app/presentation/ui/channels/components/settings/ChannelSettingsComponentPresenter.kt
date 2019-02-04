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
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelFlags
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
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
    private val updateStoreboxFlagsInteractor: UpdateStoreboxFlagsInteractor,
    private val getChannelInteractor: GetChannelInteractor,
    private val uninstallChannelInteractor: UninstallChannelInteractor
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
        runAction { it.showProgress(true) }
        getStoreboxCardLinkInteractor.run()
    }

    override fun deleteStoreboxAccountLink() {
        runAction { it.showProgress(true) }
        deleteStoreboxAccountLinkInteractor.run()
    }

    override fun updateChannelFlags(channel: Channel, flags: ChannelFlags) {
        updateStoreboxFlagsInteractor.input = UpdateStoreboxFlagsInteractor.Input(channel.id, flags)
        updateStoreboxFlagsInteractor.run()
    }

    override fun removeChannel() {
        runAction { it.showProgress(true) }
        currentChannel?.let {
            uninstallChannelInteractor.input = UninstallChannelInteractor.Input(it.id)
            uninstallChannelInteractor.run()
        }
    }

    private fun refreshChannel(channelId: Int) {
        getChannelInteractor.input = GetChannelInteractor.Input(channelId)
        getChannelInteractor.run()
    }

    /**
     * Interactor callbacks ----------------------------------------------------------------------->
     */

    override fun onGetCardsSuccessful(result: MutableList<StoreboxCreditCard>) {
        runAction {
            it.showProgress(false)
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
        runAction { v ->
            v.setOnlyDigitalReceipts(result.greenProfile)
        }
        getCreditCards()
    }

    override fun onGetProfileError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onPutProfile() {
        Timber.d("Storebox profile saved")
        runAction { v ->
            v.showProgress(false)
        }
    }

    override fun onPutProfileError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
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
        ChannelControlComponentFragment.refreshOnResume = true
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
        ChannelControlComponentFragment.refreshOnResume = true
    }

    override fun onUpdateFlagsError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }

    /**
     * GetChannelInteractor callbacks
     */

    override fun onGetChannel(channel: Channel) {
        currentChannel = channel
        runAction { v ->
            v.setupChannel(channel)
        }
    }

    override fun onGetChannelError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }

    /**
     * UninstallChannelInteractor callbacks
     */

    override fun onUninstallChannel() {
        ChannelControlComponentFragment.refreshOnResume = true
        runAction { v ->
            v.showProgress(false)
            v.broadcastCloseChannel()
            v.closeView()
        }
    }

    override fun onUninstallChannelError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}