package dk.eboks.app.presentation.ui.senders.screens.registrations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.senders.interactors.register.GetPendingInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 3/28/2018.
 * @author Christian
 * @since 3/28/2018.
 */
class PendingPresenter @Inject constructor(
    private val getPendingInteractor: GetPendingInteractor,
    private val registerInteractor: RegisterInteractor,
    private val unregisterInteractor: UnRegisterInteractor
) :
    BasePresenterImpl<PendingContract.View>(),
    PendingContract.Presenter,
    GetPendingInteractor.Output,
    RegisterInteractor.Output,
    UnRegisterInteractor.Output {

    init {
        getPendingInteractor.output = this
        registerInteractor.output = this
        unregisterInteractor.output = this
    }

    override fun loadPending() {
        getPendingInteractor.run()
    }

    override fun onRegistrationsLoaded(registrations: List<CollectionContainer>) {
        runAction { v ->
            v.showPendingRegistrations(registrations)
        }
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }

    override fun registerSender(id: Long) {
        registerInteractor.inputSender = RegisterInteractor.InputSender(id)
        registerInteractor.run()
    }

    override fun unregisterSender(id: Long) {
        unregisterInteractor.inputSender = UnRegisterInteractor.InputSender(id)
        unregisterInteractor.run()
    }

    override fun registerSegment(id: Long) {
        registerInteractor.inputSegment = RegisterInteractor.InputSegment(id)
        registerInteractor.run()
    }

    override fun unregisterSegment(id: Long) {
        unregisterInteractor.inputSegment = UnRegisterInteractor.InputSegment(id)
        unregisterInteractor.run()
    }

    override fun onSuccess() {
        runAction { v ->
            v.showRegistrationSuccess()
        }
    }
}