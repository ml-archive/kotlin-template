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
class DebugUsersComponentPresenter @Inject constructor(val appState: AppStateManager, val userSettingsManager: UserSettingsManager) : DebugUsersComponentContract.Presenter, BasePresenterImpl<DebugUsersComponentContract.View>() {

   private val u = mutableListOf<LoginState>()
    init {
        u.clear()
        u.add(LoginState(selectedUser= User(id = -1, name="Storebox (0909061345)", identity = "0909061345"),
                userName="0909061345", userPassWord="a12345", activationCode="f3M9KwDs"))
        /*
        u.add(LoginState(selectedUser= User(id = -2, name="Charlie Testb0urger (0703151319)", identity = "0703151319"),
                userName="0703151319", userPassWord="a12345", activationCode="Rg9d2X3D"))
                */

        /*
        u.add(LoginState(selectedUser= User(id = -2, name="Charlie Testb0urger (0703151319)", identity = "0703151319"),
                userName="0703151319", userPassWord="a12345", activationCode = "Lz70Kqt6"))
        */
        u.add(LoginState(selectedUser= User(id = -2, name="Charlie Testb0urger (0703161319)", identity = "0703161319"),
                userName="0703161319", userPassWord="a12345", activationCode = "Lz70Kqt6"))


        u.add(LoginState(selectedUser= User(id = -3, name="Boxie Fairshare (0805730045)", identity = "0805730045"),
                userName="0805730045", userPassWord="a12345", activationCode="Ha9y4P8J"))
        u.add(LoginState(selectedUser= User(id = -4, name="Signe Signfeature (090906-1349)", identity = "0909061349"),
                userName="0909061349", userPassWord="a12345", activationCode="c0XHs82M"))
        u.add(LoginState(selectedUser= User(id = -5, name="Payment (090906-1348)", identity = "0909061348"),
                userName="0909061348", userPassWord="a12345", activationCode="Do36SqNi"))
        u.add(LoginState(selectedUser= User(id = -6, name="Payment 2 (160773-1291)", identity = "1607731291"),
                userName="1607731291", userPassWord="a12345", activationCode="o9HMr6w5"))
        u.add(LoginState(selectedUser= User(id = -5, name="You got served (090906-1346)", identity = "0909061346"),
                userName="0909061346", userPassWord="a12345", activationCode="g8GYs92H"))
        u.add(LoginState(selectedUser= User(id = -7, name="Read receipts (2009160001)", identity = "2009160001"),
                userName="2009160001", userPassWord="a12345", activationCode="Qd63Jct0"))
        u.add(LoginState(selectedUser= User(id = -8, name="Laila Dollarmeyer (3010732572)", identity = "3010732572"),
                userName="3010732572", userPassWord="a12345", activationCode="n8HQr6e2"))
        u.add(LoginState(selectedUser= User(id = -9, name="Norway (08101500011 NO Snitflade)", identity = "08101500011"),
                userName="08101500011", userPassWord="a12345", activationCode="Rg7p4Y1G"))
        u.add(LoginState(selectedUser= User(id = -10, name="Sweden (201508101234 SE Snitflade)", identity = "201508101234"),
                userName="201508101234", userPassWord="a12345", activationCode="t6G3Cbg2"))
        u.add(LoginState(selectedUser= User(id = -11, name="Sec Level (SE) (197707096066)", identity = "197707096066"),
                userName="197707096066", userPassWord="a12345", activationCode="Rw3r0AZt"))
        u.add(LoginState(selectedUser= User(id = -12, name="Sec Level (NO) (15079411032)", identity = "15079411032"),
                userName="15079411032", userPassWord="a12345", activationCode="k8QGg7s6"))

        for(l in u) {
            val settings = userSettingsManager.get(l.selectedUser?.id!!)
            settings.lastLoginProviderId = "testcpr"
            userSettingsManager.put(settings)
        }
    }

    override fun makeList() {
        runAction { v ->
            v.showUsers(u)
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