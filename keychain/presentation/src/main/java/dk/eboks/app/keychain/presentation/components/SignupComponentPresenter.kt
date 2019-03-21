package dk.eboks.app.keychain.presentation.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import dk.eboks.app.keychain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.keychain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.keychain.interactors.user.CreateUserInteractor
import dk.eboks.app.keychain.presentation.components.providers.WebLoginPresenter
import dk.eboks.app.presentation.base.ViewController
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class SignupComponentPresenter @Inject constructor(
    private val viewController: ViewController,
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
        const val identity: String = ""
    }

    override fun confirmMail(email: String, name: String) {
        tempUser.name = name
        tempUser.setPrimaryEmail(email)
        checkSignupMailInteractor.input = CheckSignupMailInteractor.Input(email)
        checkSignupMailInteractor.run()
    }

    override fun createUser() {
        appState.state?.loginState?.userPassWord?.let {
            view { showProgress(true) }

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
            loginState.userLoginProviderId =
                if (viewController.isVerificationSucceeded) "cpr"
                else "email"

            loginState.userPassWord?.let {
                loginState.userName = tempUser.identity
                loginUserInteractor.input = LoginInteractor.Input(loginState, null)
                loginUserInteractor.run()
            }
            viewController.isVerificationSucceeded = false
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
        view {
            this as SignupComponentContract.TermsView
            showSignupCompleted()
        }
    }

    override fun onLoginActivationCodeRequired() {
        Timber.d("onloginactivatedcoderequired")
        view { showProgress(false) }
    }

    override fun onLoginDenied(error: ViewError) {
        Timber.d("onlogindenied")
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onLoginError(error: ViewError) {
        Timber.d("onloginerror")
        view {
            showProgress(false)
            showErrorDialog(error)
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
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    /**
     * CheckSsn Callbacks
     */
    override fun onCheckSsnExists(exists: Boolean) {
        view {
            this as SignupComponentContract.MMView
            ssnExists(exists)
        }
    }

    override fun onCheckSsnExistsError(error: ViewError) {
        // todo error handling
        // todo API does not work so we pretend the SSN did not exist to continue with the flow
        view {
            this as SignupComponentContract.MMView
            ssnExists(false)
        }
    }

    /**
     * CheckMail callbacks
     */
    override fun onVerifySignupMail(exists: Boolean) {
        view {
            this as SignupComponentContract.NameMailView
            showSignupMail(exists)
        }
    }

    override fun onVerifySignupMailError(error: ViewError) {
        view {
            this as SignupComponentContract.NameMailView
            showSignupMailError(error)
        }
    }

    /**
     * SetCurrentUserInteractor callbacks
     */
    override fun onSetCurrentUserSuccess() {
        viewController.isVerificationSucceeded = false
        view {
            this as SignupComponentContract.TermsView
            showSignupCompleted()
        }
    }

    override fun onSetCurrentUserError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }
}