package dk.nodes.template.presentation.navigation

import android.os.Bundle
import androidx.navigation.NavDirections

sealed class Route {
    data class DirectionId(val id: Int) : Route()

    data class Direction(val navDirections: NavDirections) : Route()

    data class Activity(
            val clazz: Class<*>,
            val args: Bundle? = null,
            val finish: Boolean = false
    ) : Route()

    data class ActivityResult(
            val clazz: Class<*>,
            val requestCode: Int,
            val args: Bundle? = null
    ) : Route()

    object Back : Route()
}