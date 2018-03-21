package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NemIdComponentPresenter @Inject constructor(val appState: AppStateManager) : NemIdComponentContract.Presenter, BasePresenterImpl<NemIdComponentContract.View>() {

    init {

    }

    override fun setup() {
        appState.state?.loginState?.selectedUser?.let { user ->
            runAction { v->v.setupLogin(user) }
        }

    }

    override fun cancelAndClose() {
        // set fallback login provider and close
        appState.state?.loginState?.selectedUser?.let { user ->
            user.lastLoginProvider?.let { provider_id ->
                Timber.e("Cancel and close called provider id = ")
                Config.getLoginProvider(provider_id)?.let { provider->
                    Timber.e("Setting lastLoginProvider to fallback provider ${provider.fallbackProvider}")
                    user.lastLoginProvider = provider.fallbackProvider
                }
            }.guard {
                Timber.e("error")
            }
        }
        runAction { v->v.close() }
    }

    override fun login(user: User) {
        user.lastLoginProvider = "nemid"
        appState.state?.currentUser = user
        appState.save()
    }
}