package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ActivationCodeComponentPresenter @Inject constructor(
    private val appConfig: AppConfig,
    private val appState: AppStateManager,
    private val loginInteractor: LoginInteractor
) :
    ActivationCodeComponentContract.Presenter,
    BasePresenterImpl<ActivationCodeComponentContract.View>(),
    LoginInteractor.Output {

    init {
        loginInteractor.output = this
    }

    override fun onViewCreated(view: ActivationCodeComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        appState.state?.loginState?.let {
            if (appConfig.isDebug && "3110276111" == it.userName) {
                runAction { v ->
                    v.setDebugUp("Cr4x3N6Q")
                }
            }
            Timber.e("selectedUser ${it.selectedUser}")
        }
    }

    override fun updateLoginState(activationCode: String?) {
        appState.state?.loginState?.let { ls ->
            /*
            ls.selectedUser?.let { user->
                user.activationCode = activationCode
                saveUsersInteractor.run()
            }
            */
            activationCode?.let {
                ls.activationCode = it
            }
        }
    }

    override fun login() {
        runAction { v -> v.showProgress(true) }
        appState.state?.loginState?.let {
            loginInteractor.input = LoginInteractor.Input(it)
            loginInteractor.run()
        }
    }

    override fun onLoginSuccess(response: AccessToken) {
        runAction { v ->
            v.proceedToApp()
        }
    }

    override fun onLoginActivationCodeRequired() {
        Timber.e("Wrong acti-code!!")
        runAction { v ->
            v.showError(error = null)
        }
    }

    override fun onLoginDenied(error: ViewError) {
        Timber.e("Login Denied!!")
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onLoginError(error: ViewError) {
        Timber.e("Login Error!!")
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}