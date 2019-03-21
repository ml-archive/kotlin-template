package dk.eboks.app.presentation

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

fun isGone() = getViewAssertion(ViewMatchers.Visibility.GONE)

fun isVisible() = getViewAssertion(ViewMatchers.Visibility.VISIBLE)

fun isInvisible() = getViewAssertion(ViewMatchers.Visibility.INVISIBLE)

private fun getViewAssertion(visibility: ViewMatchers.Visibility): ViewAssertion? {
    return ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(visibility))
}