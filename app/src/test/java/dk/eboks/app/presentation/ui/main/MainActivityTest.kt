package dk.eboks.app.presentation.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.presentation.launchActivity
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun showMainSection() {
        val scenario = launchActivity<MainActivity>()
        onView(withClassName("BottomNavigationItemView")).perform(click())
        scenario.onActivity {
            verify {
                it.showMainSection(Section.Home)
            }
        }
    }
}