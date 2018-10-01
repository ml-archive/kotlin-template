package dk.eboks.app.presentation.ui.login.components

import android.arch.lifecycle.Lifecycle
import android.os.Build
import dk.eboks.app.domain.interactors.authentication.mobileacces.ActivateDeviceInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.GenerateRSAKeyInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DeviceActivationComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val generateRSAKeyInteractor: GenerateRSAKeyInteractor,
        val activateDeviceInteractor: ActivateDeviceInteractor,
        val deleteRSAKeyInteractor: DeleteRSAKeyInteractor
) :
        DeviceActivationComponentContract.Presenter,
        GenerateRSAKeyInteractor.Output,
        ActivateDeviceInteractor.Output,
        DeleteRSAKeyInteractor.Output,
        BasePresenterImpl<DeviceActivationComponentContract.View>() {

    init {
        generateRSAKeyInteractor.output = this
        activateDeviceInteractor.output = this
        deleteRSAKeyInteractor.output = this
    }

    override fun onViewCreated(view: DeviceActivationComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        runAction { v ->
            v.setupButtons()
        }
    }

    override fun requestNemidLogin() {
        runAction { v ->
            v.requestNemidLogin()
        }
    }

    override fun activateDevice() {
        appState.state?.currentUser?.let { user ->
            generateRSAKeyInteractor.input = GenerateRSAKeyInteractor.Input(user.id.toString())
            generateRSAKeyInteractor.run()
        }
    }

    override fun skipKey() {
        runAction { v->
            v.closeDrawer()
        }
    }

    override fun onGenerateRSAKeySuccess(RSAKey: String) {
        activateDeviceInteractor.input = ActivateDeviceInteractor.Input(RSAKey)
        activateDeviceInteractor.run()
    }

    override fun onGenerateRSAKeyError(error: ViewError) {
    }

    override fun onActivateDeviceSuccess() {
        //todo
        println("succes")
    }

    override fun onActivateDeviceError(error: ViewError, RSAKey: String?) {
        RSAKey?.let {
            deleteRSAKeyInteractor.input = DeleteRSAKeyInteractor.Input(it)
            deleteRSAKeyInteractor.run()
        }
    }

    override fun onDeleteRSAKeySuccess() {

    }

    override fun onDeleteRSAKeyError(error: ViewError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}