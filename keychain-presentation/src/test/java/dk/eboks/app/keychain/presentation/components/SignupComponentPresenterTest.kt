package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import dk.eboks.app.keychain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.keychain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.keychain.interactors.user.CreateUserInteractor
import dk.eboks.app.keychain.presentation.components.providers.WebLoginPresenter
import dk.eboks.app.presentation.base.ViewController
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class SignupComponentPresenterTest {

    private val viewController = mockk<ViewController>(relaxUnitFun = true)
    private val appState = mockk<AppStateManager>(relaxUnitFun = true)
    private val createUserInteractor = mockk<CreateUserInteractor>(relaxUnitFun = true)
    private val loginUserInteractor = mockk<LoginInteractor>(relaxUnitFun = true)
    private val checkSignupMailInteractor = mockk<CheckSignupMailInteractor>(relaxUnitFun = true)
    private val checkSsnExistsInteractor = mockk<CheckSsnExistsInteractor>(relaxUnitFun = true)
    private val setCurrentUserInteractor = mockk<SetCurrentUserInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<SignupComponentContract.SignupView>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)
    private lateinit var presenter: SignupComponentPresenter

    @Before
    fun setUp() {
        presenter = SignupComponentPresenter(
            viewController,
            appState,
            createUserInteractor,
            loginUserInteractor,
            checkSignupMailInteractor,
            checkSsnExistsInteractor,
            setCurrentUserInteractor
        )
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
        WebLoginPresenter.newIdentity = null
    }

    @Test
    fun `Test confirm mail`() {
        val email = "test@test.com"
        val name = "name"
        presenter.confirmMail(email, name)
        verify {
            checkSignupMailInteractor.input = CheckSignupMailInteractor.Input(email)
            checkSignupMailInteractor.run()
        }
    }

    @Test
    fun `Test create user`() {
        val identity = "identity"
        val email = "test@test.com"
        val password = "password"
        val user = SignupComponentPresenter.tempUser
        every { appState.state?.loginState?.userPassWord } returns password
        WebLoginPresenter.newIdentity = identity
        user.setPrimaryEmail(email)
        presenter.createUser()
        verify {
            viewMock.showProgress(true)
            createUserInteractor.input = CreateUserInteractor.Input(user, password)
            createUserInteractor.run()
        }
    }

    @Test
    fun `Test login user`() {
        val password = "password"
        val loginState = LoginState(userPassWord = password)
        every { viewController.isVerificationSucceeded } returns true
        every { appState.state?.loginState } returns loginState
        presenter.loginUser()
        verify {
            loginUserInteractor.input = LoginInteractor.Input(loginState, null)
            loginUserInteractor.run()
            viewController.isVerificationSucceeded = false
            WebLoginPresenter.newIdentity = null
        }
    }

    @Test
    fun `Test verify SSN`() {
        val ssn = "ssn"
        presenter.verifySSN(ssn)
        verify {
            checkSsnExistsInteractor.input = CheckSsnExistsInteractor.Input(ssn)
            checkSsnExistsInteractor.run()
        }
    }
}