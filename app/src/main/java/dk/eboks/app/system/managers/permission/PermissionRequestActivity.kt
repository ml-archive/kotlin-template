package dk.eboks.app.system.managers.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import dk.eboks.app.R
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 22/02/18.
 */
class PermissionRequestActivity : BaseActivity() {
    @Inject lateinit var permissionManager: PermissionManager
    val PERMISSION_REQUEST = 40156

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_request)
        component.inject(this)
        requestPermissions()
    }

    override fun setupTranslations() {

    }

    private fun requestPermissions()
    {
        if(!permissionManager.requestInProgress || permissionManager.permsToCheck?.isEmpty() == null)
        {
            Timber.e("No permissions requested, aborting permission request")
            return
        }
        val perm_list = ArrayList<String>()
        for(perm in permissionManager.permsToCheck!!)
        {
            perm_list.add(perm.perm)
        }

        ActivityCompat.requestPermissions(this, perm_list.toTypedArray(), PERMISSION_REQUEST)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Timber.e("Permission request completed")
        for(i in 0..permissions.size)
        {
            Timber.e("Requested perm ${permissions[i]} = ${grantResults[i] == PackageManager.PERMISSION_GRANTED}")
            permissionManager.permsToCheck?.let { perms ->
                for(perm in perms)
                {
                    perm.wasGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED
                }
            }

        }
        permissionManager.onRequestCompleted()
    }
}