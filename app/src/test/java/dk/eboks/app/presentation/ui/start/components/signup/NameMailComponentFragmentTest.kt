package dk.eboks.app.presentation.ui.start.components.signup

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.R
import dk.eboks.app.keychain.presentation.components.SignupComponentContract
import dk.eboks.app.presentation.isGone
import dk.eboks.app.presentation.isVisible
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertFalse
import kotlinx.android.synthetic.main.fragment_signup_name_mail_component.*
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NameMailComponentFragmentTest {

    private lateinit var scenario: FragmentScenario<NameMailComponentFragment>

    @Before
    fun setUp() {
        scenario =
            launchFragmentInContainer<NameMailComponentFragment>(themeResId = R.style.AppTheme)
    }

    @After
    fun tearDown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun `Test email invalid`() {
        val email = "eboksom"
        scenario.onFragment { it.emailEt?.setText(email) }
        onView(withId(R.id.continueBtn)).check(matches(not(isEnabled())))
        scenario.onFragment { assertFalse(it.emailValid) }
    }

    @Test
    fun `Test show progress`() {

        // When
        scenario.onFragment {
            it.showProgress(false)
        }
        // Then
        onView(withId(R.id.progress)).check(matches(isGone()))
        onView(withId(R.id.scrollView)).check(matches(isVisible()))

        // When
        scenario.onFragment {
            it.showProgress(true)
        }

        // Then
        onView(withId(R.id.progress)).check(matches(isVisible()))
        onView(withId(R.id.scrollView)).check(matches(isGone()))
    }

    @Test
    fun `Test email valid and button click`() {
        // Given
        val presenter = mockk<SignupComponentContract.Presenter>(relaxUnitFun = true)
        val email = "eboks@eboks.com"
        val name = "eboks user"

        scenario.onFragment {
            it.presenter = presenter
            it.emailEt?.setText(email)
            it.nameEt.setText(name)
        }
        // When
        onView(withId(R.id.continueBtn)).perform(click())

        // Then
        verify { presenter.confirmMail(email, name) }
    }
}
