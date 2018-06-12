package dk.eboks.app.presentation.ui.components.start.login.providers.bankidno

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginPresenter
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class BankIdNOComponentPresenter @Inject constructor(appState: AppStateManager, transformTokenInteractor: TransformTokenInteractor) : WebLoginPresenter(appState, transformTokenInteractor) {
    override fun onLoginSuccess(response: AccessToken) {

        appState.state?.loginState?.selectedUser?.let { user ->
            user.lastLoginProviderId = "bankid_no"
            appState.state?.currentUser = user
            appState.save()

        }
        super.onLoginSuccess(response)
    }

//    init {
//
//    }
//
//    override fun setup() {
//        appState.state?.loginState?.selectedUser?.let { user ->
//            runAction { v->v.setupLogin(user) }
//        }
//
//    }
//
//    override fun cancelAndClose() {
//        // set fallback login provider and close
//        appState.state?.loginState?.selectedUser?.let { user ->
//            user.lastLoginProvider?.let { provider_id ->
//                Timber.e("Cancel and close called provider id = ")
//                Config.getLoginProvider(provider_id)?.let { provider->
//                    Timber.e("Setting lastLoginProvider to fallback provider ${provider.fallbackProvider}")
//                    user.lastLoginProvider = provider.fallbackProvider
//                }
//            }.guard {
//                Timber.e("error")
//            }
//        }
//        runAction { v->v.close() }
//    }
//
//    override fun login(user: User) {
//        user.lastLoginProvider = "bankid_no"
//        appState.state?.currentUser = user
//        appState.save()
//    }
}