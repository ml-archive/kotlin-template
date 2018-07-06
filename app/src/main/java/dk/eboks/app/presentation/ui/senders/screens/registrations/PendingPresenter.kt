package dk.eboks.app.presentation.ui.senders.screens.registrations

import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by Christian on 3/28/2018.
 * @author   Christian
 * @since    3/28/2018.
 */
class PendingPresenter(val appStateManager: AppStateManager,
                       val getPendingInteractor: GetPendingInteractor,
                       val registerInteractor: RegisterInteractor,
                       val unregisterInteractor: UnRegisterInteractor) :
        BasePresenterImpl<PendingContract.View>(),
        PendingContract.Presenter,
        GetPendingInteractor.Output,
        RegisterInteractor.Output,
        UnRegisterInteractor.Output {


    init {
        getPendingInteractor.output = this
        getPendingInteractor.run()
        registerInteractor.output = this
        unregisterInteractor.output = this
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