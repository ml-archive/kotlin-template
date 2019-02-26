package dk.eboks.app.presentation.ui.start.components.signup

import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import dk.eboks.app.keychain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.User
import cz.levinzonr.keychain.presentation.components.providers.WebLoginPresenter
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SignupComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val createUserInteractor: CreateUserInteractor,
    private val loginUserInteractor: LoginInteractor,
    private val checkSignupMailInteractor: CheckSignupMailInteractor,
    private val checkSsnExistsInteractor: CheckSsnExistsInteractor,
    setCurrentUserInteractor: SetCurrentUserInteractor
) :
    SignupComponentContract.Presenter,
    BasePresenterImpl<SignupComponentContract.SignupView>(),
    CreateUserInteractor.Output,
    LoginInteractor.Output,
    CheckSsnExistsInteractor.Output,
    CheckSignupMailInteractor.Output,
    SetCurrentUserInteractor.Output {

    init {
        createUserInteractor.output = this
        checkSignupMailInteractor.output = this
        loginUserInteractor.output = this
        checkSsnExistsInteractor.output = this
        setCurrentUserInteractor.output = this
    }

    companion object {
        val tempUser: User = User()
        val identity: String = ""
    }

    override fun confirmMail(email: String, name: String) {
        tempUser.name = name
        tempUser.setPrimaryEmail(email)
        checkSignupMailInteractor.input = CheckSignupMailInteractor.Input(email)
        checkSignupMailInteractor.run()
    }

    override fun createUser() {
        appState.state?.loginState?.userPassWord?.let {
            runAction { v -> v.showProgress(true) }

            WebLoginPresenter.newIdentity?.let { identity ->
                tempUser.identity = identity
            }.guard { tempUser.identity = tempUser.getPrimaryEmail() }

            createUserInteractor.input = CreateUserInteractor.Input(tempUser, it)
            createUserInteractor.run()
        }
    }

    override fun setPassword(password: String) {
        appState.state?.loginState?.userPassWord = password
    }

    override fun loginUser() {
        appState.state?.loginState?.let { loginState ->
            // we have a token, this is a verified user, set cpr instead of email as last login provider
            if (VerificationComponentFragment.verificationSucceeded)
                loginState.userLoginProviderId = "cpr"
            else
                loginState.userLoginProviderId = "email"

            loginState.userPassWord?.let { password ->
                loginState.userPassWord = password

                loginState.userName = tempUser.identity
                loginUserInteractor.input = LoginInteractor.Input(loginState, null)
                loginUserInteractor.run()
            }
            VerificationComponentFragment.verificationSucceeded = false
            WebLoginPresenter.newIdentity = null
        }
    }

    // Mina meddelan
    override fun verifySSN(ssn: String) {
        checkSsnExistsInteractor.input = CheckSsnExistsInteractor.Input(ssn)
        checkSsnExistsInteractor.run()
    }

    /**
     * Login callbacks
     */
    override fun onLoginSuccess(response: AccessToken) {
        Timber.d("success")
        runAction { v ->
            v as SignupComponentContract.TermsView
            v.showSignupCompleted()
        }
    }

    override fun onLoginActivationCodeRequired() {
        Timber.d("onloginactivatedcoderequired")
        runAction { v -> v.showProgress(false) }
    }

    override fun onLoginDenied(error: ViewError) {
        Timber.d("onlogindenied")
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onLoginError(error: ViewError) {
        Timber.d("onloginerror")
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    /**
     * Create user callbacks
     */
    override fun onCreateUser(user: User) {
        // if(!VerificationComponentFragment.verificationSucceeded)
        appState.state?.loginState?.token = null
        loginUser()
    }

    override fun onCreateUserError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    /**
     * CheckSsn Callbacks
     */
    override fun onCheckSsnExists(exists: Boolean) {
        runAction { v ->
            v as SignupComponentContract.MMView
            v.ssnExists(exists)
        }
    }

    override fun onCheckSsnExists(error: ViewError) {
        // todo error handling
        // todo API does not work so we pretend the SSN did not exist to continue with the flow
        runAction { v ->
            v as SignupComponentContract.MMView
            v.ssnExists(false)
        }
    }

    /**
     * CheckMail callbacks
     */
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

    /**
     * SetCurrentUserInteractor callbacks
     */
    override fun onSetCurrentUserSuccess() {
        VerificationComponentFragment.verificationSucceeded = false
        runAction { v ->
            v as SignupComponentContract.TermsView
            v.showSignupCompleted()
        }
    }

    override fun onSetCurrentUserError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}