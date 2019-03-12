package dk.eboks.app.profile.presentation.ui.components.myinfo

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.login.ContactPoint
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.profile.interactors.GetUserProfileInteractor
import dk.eboks.app.profile.interactors.SaveUserInteractor
import dk.eboks.app.profile.interactors.UpdateUserInteractor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MyInfoComponentPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val saveUserInteractor: SaveUserInteractor = mockk(relaxUnitFun = true)
    private val updateUserInteractor: UpdateUserInteractor = mockk(relaxUnitFun = true)
    private val getUserProfileInteractor: GetUserProfileInteractor = mockk(relaxUnitFun = true)

    private val view: MyInfoComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: MyInfoComponentPresenter

    private val user = User(mobilenumber = ContactPoint("123"))

    @Before
    fun setUp() {
        presenter = MyInfoComponentPresenter(appStateManager, saveUserInteractor, updateUserInteractor, getUserProfileInteractor)
        presenter.onViewCreated(view, lifecycle)

        every { appStateManager.state } returns AppState(currentUser = user)

        presenter.setup()

        assert(user == presenter.currentUser)
    }


    @Test
    fun `Show User test`() {

        val contact = ContactPoint("1", verified = false)
        val user = User(verified = true, mobilenumber = contact)

        every { appStateManager.state } returns AppState(currentUser = user)

        presenter.setup()

        verify {
            view.setName(user.name)
            view.setNewsletter(user.newsletter)
            view.setSaveEnabled(false)
            view.setMobileNumber("1", contact.verified)
        }
    }

    @Test
    fun `OSave Test`() {
        presenter.onSaveUser(user, 1)
        verify {
            with(view) {
                setSaveEnabled(false)
                showProgress(false)
                showToast(Translation.profile.yourInfoWasSaved)
            }
        }
    }

    @Test
    fun `On Update Profile`() {
        presenter.onUpdateProfile()
        verify {
            saveUserInteractor.input = SaveUserInteractor.Input(user)
            saveUserInteractor.run()
        }
    }
}