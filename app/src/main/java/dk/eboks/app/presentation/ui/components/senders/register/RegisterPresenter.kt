package dk.eboks.app.presentation.ui.components.senders.register

import dk.eboks.app.domain.interactors.sender.register.RegisterSenderGroupInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 3/22/2018.
 * @author   Christian
 * @since    3/22/2018.
 */
class RegisterPresenter @Inject constructor(val appState: AppStateManager, val registerSenderGroupInteractor: RegisterSenderGroupInteractor) :
        RegistrationContract.Presenter, BasePresenterImpl<RegistrationContract.View>(),
        RegisterSenderGroupInteractor.Output {

    override fun registerSenderGroup(senderId: Long, sendergroup: SenderGroup) {
        registerSenderGroupInteractor.input = RegisterSenderGroupInteractor.Input(senderId, sendergroup)
    }

    override fun onSuccess() {
    }

    override fun onError(msg: String) {
    }

}