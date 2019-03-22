package dk.eboks.app.presentation.ui.main

import androidx.test.core.app.ActivityScenario
import org.junit.Test

import org.junit.Assert.*

class MainActivityTest {

    @Test
    fun showMainSection() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity {

        }
    }
}