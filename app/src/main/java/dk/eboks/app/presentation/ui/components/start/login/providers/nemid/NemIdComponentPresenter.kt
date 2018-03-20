package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NemIdComponentPresenter @Inject constructor(val appState: AppStateManager) : NemIdComponentContract.Presenter, BasePresenterImpl<NemIdComponentContract.View>() {

    init {

    }

    override fun cancelAndClose() {
        // set fallback login provider and close
        appState.state?.loginState?.selectedUser?.let { user ->
            user.lastLoginProvider?.let { provider_id ->
                Config.getLoginProvider(provider_id)?.let { provider->
                    Timber.e("Setting lastLoginProvider to fallback provider ${provider.fallbackProvider}")
                    user.lastLoginProvider = provider.fallbackProvider
                }
            }
        }
        runAction { v->v.close() }
    }

    override fun login(user: User) {
        appState.state?.currentUser = user
        appState.save()
    }
}