package dk.eboks.app.presentation.ui.senders.components.register

import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 3/22/2018.
 * @author Christian
 * @since 3/22/2018.
 */
class RegisterPresenter @Inject constructor(
    private val registerInteractor: RegisterInteractor,
    private val unRegisterInteractor: UnRegisterInteractor
) :
    RegistrationContract.Presenter, BasePresenterImpl<RegistrationContract.View>(),
    RegisterInteractor.Output,
    UnRegisterInteractor.Output {

    init {
        registerInteractor.output = this
        unRegisterInteractor.output = this
    }

    override fun registerSender(sender: Sender) {
        registerInteractor.inputSender = RegisterInteractor.InputSender(sender.id)
        registerInteractor.run()
    }

    override fun registerSenderGroup(senderId: Long, sendergroup: SenderGroup) {
        Timber.i("registerSenderGroup")
        registerInteractor.inputSenderGroup =
            RegisterInteractor.InputSenderGroup(senderId, sendergroup)
        registerInteractor.run()
    }

    override fun registerSegment(segment: Segment) {
        registerInteractor.inputSegment = RegisterInteractor.InputSegment(segment.id)
        registerInteractor.run()
    }

    override fun unregisterSender(sender: Sender) {
        unRegisterInteractor.inputSender = UnRegisterInteractor.InputSender(sender.id)
        unRegisterInteractor.run()
    }

    override fun unregisterSenderGroup(senderId: Long, sendergroup: SenderGroup) {
        Timber.i("unregisterSenderGroup")
        unRegisterInteractor.inputSenderGroup =
            UnRegisterInteractor.InputSenderGroup(senderId, sendergroup)
        unRegisterInteractor.run()
    }

    override fun unregisterSegment(segment: Segment) {
        unRegisterInteractor.inputSegment = UnRegisterInteractor.InputSegment(segment.id)
        unRegisterInteractor.run()
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