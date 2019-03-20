package dk.eboks.app.presentation.ui.start.components.signup

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.presentation.ui.start.screens.StartActivity
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
        val scenario = ActivityScenario.launch(StartActivity::class.java)
    }
}
