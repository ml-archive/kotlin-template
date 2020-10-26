package dk.nodes.template.presentation.navigation

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentNavigationHandler(val fragment: Fragment) : NavigationHandler {

    override fun handle(route: Route) {
        when (route) {
            is Route.ActivityResult -> handleActivityForResult(route)
            is Route.Activity -> handleActivity(route)
            is Route.Back -> fragment.findNavController().navigateUp()
            is Route.Direction -> handleDirection(route)
            is Route.DirectionId -> handleDirectionId(route)
        }
    }

    private fun handleActivityForResult(route: Route.ActivityResult) {
        val intent = Intent(fragment.context, route.clazz).apply {
            route.args?.let(this::putExtras)
        }
        fragment.startActivityForResult(intent, route.requestCode)
    }

    private fun handleDirectionId(directionId: Route.DirectionId) {
        fragment.findNavController().navigate(directionId.id)
    }

    private fun handleDirection(direction: Route.Direction) {
        fragment.findNavController().navigate(direction.navDirections)
    }

    private fun handleActivity(route: Route.Activity) {
        val intent = Intent(fragment.context, route.clazz).apply {
            route.args?.let(this::putExtras)
        }
        if (route.finish) fragment.activity?.finish()
        fragment.startActivity(intent)
    }
}