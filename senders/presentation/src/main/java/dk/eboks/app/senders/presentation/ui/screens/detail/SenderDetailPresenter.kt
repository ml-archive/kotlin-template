package dk.eboks.app.senders.presentation.ui.screens.detail

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.domain.senders.interactors.GetSenderDetailInteractor
import dk.eboks.app.domain.senders.interactors.register.GetSenderRegistrationLinkInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 * @author bison
 * @since 20-05-2017.
 */

internal class SenderDetailPresenter @Inject constructor(
        val getSenderRegistrationLinkInteractor: GetSenderRegistrationLinkInteractor,
        val appStateManager: AppStateManager,
        val getSenderDetailInteractor: GetSenderDetailInteractor,
        val registerInteractor: RegisterInteractor,
        val unregisterInteractor: UnRegisterInteractor) :
        SenderDetailContract.Presenter, BasePresenterImpl<SenderDetailContract.View>(),
        GetSenderDetailInteractor.Output,
        RegisterInteractor.Output,
        UnRegisterInteractor.Output,
        GetSenderRegistrationLinkInteractor.Output {

    init {
        getSenderDetailInteractor.output = this
        registerInteractor.output = this
        unregisterInteractor.output = this
        getSenderRegistrationLinkInteractor.output = this
    }

    override fun loadSender(id: Long) {
        runAction { it.toggleLoading(true) }
        getSenderDetailInteractor.input = GetSenderDetailInteractor.Input(id)
        getSenderDetailInteractor.run()
    }

    override fun registerSender(id: Long) {
        registerInteractor.inputSender = RegisterInteractor.InputSender(id)
        registerInteractor.run()
    }

    override fun unregisterSender(id: Long) {
        unregisterInteractor.inputSender = UnRegisterInteractor.InputSender(id)
        unregisterInteractor.run()
    }

    override fun onGetSender(sender: Sender) {
        view {
            showSender(sender)
            toggleLoading(false)

        }
    }

    override fun onGetSenderError(error: ViewError) {
        view {
            showErrorDialog(error)
            toggleLoading(false)
        }

    }

    override fun onSuccess() {
        Timber.i("Success")
        view { showSuccess() }
    }

    override fun onError(error: ViewError) {
        view { showError(error.message ?: "") }
    }

    override fun registerViaLink(id: Long) {
        getSenderRegistrationLinkInteractor.input = GetSenderRegistrationLinkInteractor.Input(id)
        getSenderRegistrationLinkInteractor.run()
    }

    override fun onLinkLoaded(link: Link) {

    }

    override fun onLinkLoadingError(viewError: ViewError) {

    }
}