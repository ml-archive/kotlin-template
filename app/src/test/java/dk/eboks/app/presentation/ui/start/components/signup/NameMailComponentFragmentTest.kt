package dk.eboks.app.presentation.ui.start.components.signup

import android.widget.Button
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.R
import dk.eboks.app.keychain.presentation.components.SignupComponentContract
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NameMailComponentFragmentTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test() {

        val scenario = launchFragmentInContainer<NameMailComponentFragment>()
        val presenter = mockk<SignupComponentContract.Presenter>(relaxUnitFun = true)
        scenario.onFragment {
            it.view?.findViewById<Button>(R.id.continueBtn)?.isEnabled = true
            it.presenter = presenter
        }

        onView(withId(R.id.continueBtn)).check(matches(ViewMatchers.isEnabled()))
    }
}
