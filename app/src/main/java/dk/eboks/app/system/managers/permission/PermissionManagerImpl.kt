package dk.eboks.app.system.managers.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor

/**
 * Created by bison on 22/02/18.
 */
class PermissionManagerImpl(val executor: Executor, val context: Context, val uiManager: UIManager) : PermissionManager {
    override var permsToCheck: MutableList<PermissionManager.Permission>? = null
    override var requestInProgress = false

    override fun checkPermission(perm: String): Boolean {
        return ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
    }

    override fun checkPermissions(perms : List<String>): Boolean {
        for(perm in perms) {
            if(ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_DENIED)
                return false
        }
        return true
    }

    override fun requestPermissions(perms : List<String>): List<PermissionManager.Permission>? {
        requestInProgress = true
        if(permsToCheck == null)
            permsToCheck = ArrayList()
        else
            permsToCheck?.clear()

        for(perm in perms)
        {
            permsToCheck?.add(PermissionManager.Permission(perm, false))
        }
        uiManager.showPermissionRequestScreen()
        executor.sleepUntilSignalled("permissionRequestCompleted")
        // check perms
        requestInProgress = false
        return permsToCheck
    }

    override fun requestPermission(perm : String): Boolean {
        val perms = requestPermissions(listOf(perm))
        perms?.guard { return false }
        perms?.forEach {
            if(it.perm == perm && it.wasGranted)
                return true
        }
        return false
    }

    override fun onRequestCompleted() {
        executor.signal("permissionRequestCompleted")
    }
}