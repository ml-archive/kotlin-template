package dk.eboks.app.presentation.ui.screens.start

import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class StartPresenter(val appStateManager: AppStateManager, val bootstrapInteractor: BootstrapInteractor) :
        StartContract.Presenter,
        BasePresenterImpl<StartContract.View>(),
        BootstrapInteractor.Output {

    init {
        bootstrapInteractor.output = this
        runAction { v -> v.performVersionControl() }
    }

    override fun proceed() {
        Timber.e("Ready to proceed")

        bootstrapInteractor.run()
    }

    override fun onBootstrapDone() {
        Timber.e("Boostrap done")
        runAction { v ->
            v.showWelcomeComponent()
        }
        /*
        loginInteractor.input = LoginInteractor.Input(UserInfo(identity = "0703151319", identityType = "P", nationality = "DK", pincode = "a12345", activationCode = "Rg9d2X3D"))
        loginInteractor.run()
        */
    }

    override fun onBootstrapError(msg: String) {
        Timber.e("Boostrap error: $msg")
    }
}