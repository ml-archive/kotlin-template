package dk.eboks.app.presentation.ui.login.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.authentication.mobileacces.ActivateDeviceInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.GenerateRSAKeyInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DeviceActivationComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val generateRSAKeyInteractor: GenerateRSAKeyInteractor,
        private val activateDeviceInteractor: ActivateDeviceInteractor,
        private val deleteRSAKeyInteractor: DeleteRSAKeyInteractor
) :
    DeviceActivationComponentContract.Presenter,
    GenerateRSAKeyInteractor.Output,
    ActivateDeviceInteractor.Output,
    DeleteRSAKeyInteractor.Output,
    BasePresenterImpl<DeviceActivationComponentContract.View>() {

    var loading = false
        set(value) {
            field = value
            runAction { it.showProgress(value) }
        }

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
        if (!loading) {
            appState.state?.currentUser?.let { user ->
                loading = true
                generateRSAKeyInteractor.input = GenerateRSAKeyInteractor.Input(user.id.toString())
                generateRSAKeyInteractor.run()
            }
        }
    }

    override fun onGenerateRSAKeySuccess(RSAKey: String) {
        activateDeviceInteractor.input = ActivateDeviceInteractor.Input(RSAKey)
        activateDeviceInteractor.run()
    }

    override fun onGenerateRSAKeyError(error: ViewError) {
        loading = false
    }

    override fun onActivateDeviceSuccess() {
        // todo
        Timber.e("onActivateDevice  success")
        loading = false
        runAction { view ->
            view.login()
        }
    }

    override fun onActivateDeviceError(error: ViewError, RSAKey: String?) {
        deleteRSAKeyInteractor.run()
    }

    override fun onDeleteRSAKeySuccess() {
        Timber.e("onDeleteRSAKeyError  success")
        loading = false
    }

    override fun onDeleteRSAKeyError(error: ViewError) {
        Timber.e("onDeleteRSAKeyError  failed")
        loading = false
    }
}