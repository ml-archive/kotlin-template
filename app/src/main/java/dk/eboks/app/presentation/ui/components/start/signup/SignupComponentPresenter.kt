package dk.eboks.app.presentation.ui.components.start.signup

import android.util.Log
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import kotlinx.coroutines.experimental.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SignupComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val createUserInteractor: CreateUserInteractor,
        val loginUserInteractor: LoginInteractor,
        val verifySignupMailInteractor: CheckSignupMailInteractor
) :
        SignupComponentContract.Presenter,
        BasePresenterImpl<SignupComponentContract.SignupView>(),
        CreateUserInteractor.Output,
        LoginInteractor.Output,
        CheckSignupMailInteractor.Output {

    init {
        createUserInteractor.output = this
        verifySignupMailInteractor.output = this
        loginUserInteractor.output = this
    }

    override fun confirmMail(email: String, name: String) {
        tempUser.name = name
        tempUser.setPrimaryEmail(email)
        verifySignupMailInteractor.input = CheckSignupMailInteractor.Input(email)
        verifySignupMailInteractor.run()
    }

    override fun onVerifySignupMail(exists: Boolean) {
        runAction { v ->
            v as SignupComponentContract.NameMailView
            v.showSignupMail(exists)
        }
    }

    override fun onVerifySignupMail(error: ViewError) {
        runAction { v ->
            v as SignupComponentContract.NameMailView
            v.showSignupMailError(error)
        }
    }

    override fun createUser() {
        appState.state?.loginState?.userPassWord?.let {
            createUserInteractor.input = CreateUserInteractor.Input(tempUser, it)
            createUserInteractor.run()
        }
    }

    override fun setPassword(password: String) {
        appState.state?.loginState?.userPassWord = password
    }

    override fun createUserAndLogin() {
        tempUser.lastLoginProvider = "email"
        appState.state?.currentUser = tempUser
        appState.save()
        appState.state?.loginState?.let { loginState ->
            loginState.userPassWord?.let { password ->
                loginState.userPassWord = password
                loginState.userName = appState.state?.currentUser?.cpr?: appState.state?.currentUser?.getPrimaryEmail()
                loginState.token = null
                loginUserInteractor.input = LoginInteractor.Input(loginState)
                loginUserInteractor.run()
            }
        }
    }


    fun login() {
        runAction { v ->
            v as SignupComponentContract.CompletedView
            v.doLogin()
        }
    }

    //todo blocked untill the login/create user actually works. will try to login regardless to be able to follow the flow of the app - this needs to be changed
    override fun onLoginSuccess(response: AccessToken) {
        Timber.d("success")
        login()
    }

    override fun onLoginActivationCodeRequired() {
        Timber.d("onloginactivatedcoderequired")
        login()
    }

    override fun onLoginDenied(error: ViewError) {
        Timber.d("onlogindenied")
        login()
    }

    override fun onLoginError(error: ViewError) {
        Timber.d("onloginerror")
        login()
    }

    override fun onCreateUser(user: User) {
        runAction { v ->
            v as SignupComponentContract.TermsView
            v.showUserCreated()
        }
    }

    override fun onCreateUserError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
            //todo since the api does not work, we need this to continue the flow
            v as SignupComponentContract.TermsView
            v.showUserCreated()
        }
    }

    companion object {
        val tempUser: User = User()
    }

    override fun setActivationCode(activationCode: String) {
        appState.state?.loginState?.activationCode = activationCode
    }
}