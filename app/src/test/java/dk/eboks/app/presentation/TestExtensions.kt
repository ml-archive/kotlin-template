package dk.eboks.app.presentation

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher

fun isGone() = ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)

fun isVisible() = ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)

fun isInvisible() = ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)

fun isRefreshing(): Matcher<View> =
    object : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("is refreshing")
        }

        override fun matchesSafely(view: SwipeRefreshLayout): Boolean {
            return view.isRefreshing
        }
    }

inline fun <reified A : Activity> launchActivity(intent: Intent? = null): ActivityScenario<A> =
    when (intent) {
        null -> ActivityScenario.launch(A::class.java)
        else -> ActivityScenario.launch(intent)
    }