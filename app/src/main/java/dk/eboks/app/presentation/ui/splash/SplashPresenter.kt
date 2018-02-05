package dk.eboks.app.presentation.ui.splash

import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.LoginInteractor
import dk.eboks.app.domain.models.request.UserInfo
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class SplashPresenter(val bootstrapInteractor: BootstrapInteractor, val loginInteractor: LoginInteractor) : SplashContract.Presenter, BasePresenterImpl<SplashContract.View>(),
        LoginInteractor.Output,
        BootstrapInteractor.Output {

    init {
        bootstrapInteractor.output = this
        loginInteractor.output = this
        runAction { v -> v.performVersionControl() }
    }

    override fun proceed() {
        Timber.e("Ready to proceed")
        runAction { v ->
            v.startMain()
        }
        //bootstrapInteractor.run()
    }

    override fun onLogin() {
        Timber.e("onLogin")
    }

    override fun onLoginError(msg: String) {
        runAction{ view?.showError(msg) }
    }

    override fun onBootstrapDone() {
        Timber.e("Boostrap done")
        loginInteractor.input = LoginInteractor.Input(UserInfo(identity = "0703151319", identityType = "P", nationality = "DK", pincode = "a12345", activationCode = "Rg9d2X3D"))
        loginInteractor.run()
    }

    override fun onBootstrapError(msg: String) {
        Timber.e("Boostrap error: $msg")
    }
}