package dk.eboks.app.presentation.ui.components.start.signup

import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
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
        CheckSignupMailInteractor.Output {

    init {
        createUserInteractor.output = this
        verifySignupMailInteractor.output = this
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
        appState.state?.loginState?.userPassWord?.let { password ->
            appState.state?.currentUser?.name?.let { username ->
                loginUserInteractor.input = LoginInteractor.Input(username, password, null)
            }
        }
    }

    override fun onCreateUser(user: User, numberOfUsers: Int) {
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
}