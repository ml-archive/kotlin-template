package dk.eboks.app.keychain.presentation.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User

import dk.eboks.app.keychain.interactors.user.DeleteUserInteractor
import dk.eboks.app.keychain.interactors.user.GetUsersInteractor
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserCarouselComponentPresenterTest {

    private val appStateManager = mockk<AppStateManager>()
    private val userSettingsManager = mockk<UserSettingsManager>()
    private val getUsersInteractor = mockk<GetUsersInteractor>()
    private val deleteUserInteractor = mockk<DeleteUserInteractor>()

    private val viewMock = mockk<UserCarouselComponentContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)
    private val appState = AppState(loginState = LoginState())
    private val userMock = User(id = 135153)
    private lateinit var presenter: UserCarouselComponentPresenter

    @Before
    fun setUp() {
        every { getUsersInteractor.output = any() } just Runs
        every { deleteUserInteractor.output = any() } just Runs
        every { getUsersInteractor.run() } just Runs
        every { deleteUserInteractor.run() } just Runs
        every { appStateManager.state } returns appState
        every { appStateManager.save() } just Runs
        every { deleteUserInteractor.input = any() } just Runs

        presenter = UserCarouselComponentPresenter(
            appStateManager,
            userSettingsManager,
            getUsersInteractor,
            deleteUserInteractor
        )
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test request users`() {
        presenter.requestUsers()
        verify { getUsersInteractor.run() }
    }

    @Test
    fun `Test login`() {
        presenter.login(userMock)
        verify { viewMock.openLogin() }
    }

    @Test
    fun `Test add another user`() {
        presenter.addAnotherUser()
        verify { viewMock.openLogin() }
    }

    @Test
    fun `Test clear selected user`() {
        presenter.clearSelectedUser()
        verify { appStateManager.save() }
    }

    @Test
    fun `Test delete`() {
        presenter.deleteUser(userMock)
        verify {
            deleteUserInteractor.input = DeleteUserInteractor.Input(userMock)
            deleteUserInteractor.run()
        }
    }
}