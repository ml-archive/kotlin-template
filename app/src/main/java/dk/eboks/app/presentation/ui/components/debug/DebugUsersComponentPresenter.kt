package dk.eboks.app.presentation.ui.components.debug

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 6/15/2018.
 * @author   Christian
 * @since    6/15/2018.
 */
class DebugUsersComponentPresenter @Inject constructor(val appState: AppStateManager) : DebugUsersComponentContract.Presenter, BasePresenterImpl<DebugUsersComponentContract.View>() {

   private val u = mutableListOf<LoginState>()
    init {
        u.clear()
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Storebox (0909061345)"),
                userName="0909061345", userPassWord="a12345", activationCode="f3M9KwDs"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Charlie Testb0urger (0703151319)"),
                userName="0703151319", userPassWord="a12345", activationCode="Rg9d2X3D"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Boxie Fairshare (0805730045)"),
                userName="0805730045", userPassWord="a12345", activationCode="Ha9y4P8J"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Signe Signfeature (090906-1349)"),
                userName="0909061349", userPassWord="a12345", activationCode="c0XHs82M"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Payment (090906-1348)"),
                userName="0909061348", userPassWord="a12345", activationCode="Do36SqNi"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Payment 2 (160773-1291)"),
                userName="1607731291", userPassWord="a12345", activationCode="o9HMr6w5"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Read receipts (2009160001)"),
                userName="2009160001", userPassWord="a12345", activationCode="Qd63Jct0"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Laila Dollarmeyer (3010732572)"),
                userName="3010732572", userPassWord="a12345", activationCode="n8HQr6e2"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Norway (08101500011 NO Snitflade)"),
                userName="08101500011", userPassWord="a12345", activationCode="Rg7p4Y1G"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Sweden (201508101234 SE Snitflade)"),
                userName="201508101234", userPassWord="a12345", activationCode="t6G3Cbg2"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Sec Level (SE) (197707096066)"),
                userName="197707096066", userPassWord="a12345", activationCode="Rw3r0AZt"))
        u.add(LoginState(selectedUser= User(lastLoginProviderId="testcpr", name="Sec Level (NO) (15079411032)"),
                userName="15079411032", userPassWord="a12345", activationCode="k8QGg7s6"))
    }

    override fun makeList() {
        runAction { v ->
            v.showUsers(u)
        }
    }

    override fun updateLoginState(loginState: LoginState) {
        appState.state?.loginState?.let { ls ->
            ls.userName = loginState.userName
            ls.userPassWord = loginState.userPassWord
            ls.selectedUser = loginState.selectedUser
            ls.userLoginProviderId = loginState.userLoginProviderId
            ls.activationCode = loginState.activationCode
        }
        appState.save()
    }
}