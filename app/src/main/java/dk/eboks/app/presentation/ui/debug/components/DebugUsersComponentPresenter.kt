package dk.eboks.app.presentation.ui.debug.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 6/15/2018.
 * @author   Christian
 * @since    6/15/2018.
 */
class DebugUsersComponentPresenter @Inject constructor(val appState: AppStateManager, val userSettingsManager: UserSettingsManager, val testLoginStates : MutableList<LoginState>) : DebugUsersComponentContract.Presenter, BasePresenterImpl<DebugUsersComponentContract.View>() {
    override fun makeList() {
        runAction { v ->
            v.showUsers(testLoginStates)
        }
    }

    override fun updateLoginState(ls: LoginState) {
        appState.state?.loginState?.let { loginState ->
            loginState.userName = ls.userName
            loginState.userPassWord = ls.userPassWord
            loginState.selectedUser = ls.selectedUser
            loginState.userLoginProviderId = ls.userLoginProviderId
            loginState.activationCode = ls.activationCode
        }
        appState.save()
    }
}