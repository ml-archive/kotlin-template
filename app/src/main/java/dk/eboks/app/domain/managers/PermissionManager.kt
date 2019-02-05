package dk.eboks.app.domain.managers

/**
 * Created by bison on 22/02/18.
 */
interface PermissionManager {
    var permsToCheck: MutableList<Permission>?
    var requestInProgress: Boolean

    fun checkPermission(perm: String): Boolean
    fun checkPermissions(perms: List<String>): Boolean
    fun requestPermission(perm: String): Boolean
    fun requestPermissions(perms: List<String>): List<PermissionManager.Permission>?

    fun onRequestCompleted()

    data class Permission(var perm: String, var wasGranted: Boolean)
}