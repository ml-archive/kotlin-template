package dk.eboks.app.presentation.ui.senders.screens.registrations

import dk.eboks.app.domain.senders.interactors.register.GetRegistrationsInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Registrations
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 3/28/2018.
 * @author Christian
 * @since 3/28/2018.
 */
class RegistrationsPresenter @Inject constructor(registrationsInteractor: GetRegistrationsInteractor) :
    BasePresenterImpl<RegistrationsContract.View>(),
    RegistrationsContract.Presenter,
    GetRegistrationsInteractor.Output {

    init {
        registrationsInteractor.output = this
        registrationsInteractor.run()
    }

    override fun onRegistrationsLoaded(registrations: Registrations) {
        runAction { v ->
            v.showRegistrations(registrations)
        }
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }
}