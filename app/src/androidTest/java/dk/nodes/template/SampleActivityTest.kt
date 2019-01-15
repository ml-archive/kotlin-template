package dk.nodes.template

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import dk.nodes.template.presentation.ui.main.MainActivity
import dk.nodes.template.presentation.ui.main.SampleActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
 Created by Moch
 */
@RunWith(AndroidJUnit4::class)
class SampleActivityTest {


    /*
    This is a UI sample test class show-casing how to make UI test on most views.
    The Aura and Linak Desk App are both close to 100% full UI test coverage, so take a look at them for more examples
     */

    //Tells the UI Test that we'll start and tests in this Activity.
    @Rule
    @JvmField
    var mainActivityActivity = ActivityTestRule<MainActivity>(MainActivity::class.java)


    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun writeAndSaveMessageTest() {

        // 1) Lets write something in the EditText
        onView(withId(R.id.edittext))
                .check(matches(isDisplayed()))
                .perform(clearText())
                .perform(typeText("Hello!"))


        // 2) Lets press on the save button
        onView(withId(R.id.saveButton))
                .check(matches(isDisplayed()))
                .perform(click())


        //If you want to pause the UI test for what ever reason you can sleep the thread
        // Thread.sleep(2000)

        // 3) We now check if our message has been saved into the TextView
        onView(withId(R.id.textview))
                .check(matches(isDisplayed()))
                .check(matches(withText("Hello!")))

    }

    @Test
    fun openActivityTest() {

        // 1) Lets open a new Activity
        onView(withId(R.id.buttonOpenActivity)).perform(click())

        // 2) Check if the activity has started and is showing
        intended(hasComponent(SampleActivity::class.java.name))

    }

    @Test
    fun openAlertDialogTest() {

        // 1) Lets open a AlertDialog
        onView(withId(R.id.buttonOpenDialog)).perform(click())

        // 2) Check if the AlertDialog is showing by looking for something in it.
        // Here we check if the AlertDialog Title is showing.

        onView(withText("Hello Ui Test!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

        //3 Lets press on the cancel button by finding the button with text.
        onView(withText("Cancel")).check(matches(isDisplayed())).perform(click())


        //4 Lets open the AlertDialog again and press on the OK button by the buttons ID
        onView(withId(R.id.buttonOpenDialog)).perform(click())
        onView(withId(android.R.id.button1)).check(matches(isDisplayed())).perform(click())
    }

    @After
    fun tearDown() {
        Intents.release()
    }

}