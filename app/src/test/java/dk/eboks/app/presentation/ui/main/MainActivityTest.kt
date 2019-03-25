package dk.eboks.app.presentation.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.presentation.launchActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun showMainSection() {
        val scenario = launchActivity<MainActivity>()
//        onView(withClassName("BottomNavigationItemView")).perform(click())
//        scenario.onActivity {
//            verify {
//                it.showMainSection(Section.Home)
//            }
//        }
    }
}