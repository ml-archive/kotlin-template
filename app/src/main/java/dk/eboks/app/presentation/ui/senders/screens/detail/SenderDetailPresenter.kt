package dk.eboks.app.presentation.ui.senders.screens.detail

import dk.eboks.app.domain.senders.interactors.GetSenderDetailInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 * @author bison
 * @since 20-05-2017.
 */
class SenderDetailPresenter @Inject constructor(
    private val getSenderDetailInteractor: GetSenderDetailInteractor,
    private val registerInteractor: RegisterInteractor,
    private val unregisterInteractor: UnRegisterInteractor
) :
    SenderDetailContract.Presenter, BasePresenterImpl<SenderDetailContract.View>(),
    GetSenderDetailInteractor.Output,
    RegisterInteractor.Output,
    UnRegisterInteractor.Output {

    init {
        getSenderDetailInteractor.output = this
        registerInteractor.output = this
        unregisterInteractor.output = this
    }

    override fun loadSender(id: Long) {
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
        runAction { v ->
            v.showSender(sender)
        }
    }

    override fun onGetSenderError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onSuccess() {
        Timber.i("Success")
        runAction { v ->
            v.showSuccess()
        }
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showError(error.message ?: "")
        }
    }
}