package dk.eboks.app.system.managers.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
        requestThemPermissions()
    }

    override fun setupTranslations() {

    }

    private fun requestThemPermissions()
    {
        if(!permissionManager.requestInProgress || permissionManager.permsToCheck?.isEmpty() == null)
        {
            Timber.e("No permissions requested, aborting permission request")
            finish()
            return
        }
        val perm_list = ArrayList<String>()
        for(perm in permissionManager.permsToCheck!!)
        {
            perm_list.add(perm.perm)
        }


        val perm_array = perm_list.toTypedArray()
        /*
        for(str in perm_array)
            Timber.e("perm array entry: $str")
        */
        //val PERMISSIONS : Array<out String> = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
        ActivityCompat.requestPermissions(this, perm_array, PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_REQUEST) {
            //Timber.e("Permission request completed")
            for (i in 0..permissions.size - 1) {
                Timber.e("Requested perm ${permissions[i]} = ${grantResults[i] == PackageManager.PERMISSION_GRANTED}")
                permissionManager.permsToCheck?.let { perms ->
                    perms[i].wasGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED
                }
            }

            permissionManager.permsToCheck?.forEach { perm ->
                if(ContextCompat.checkSelfPermission(this, perm.perm) == PackageManager.PERMISSION_GRANTED)
                {
                    perm.wasGranted = true
                }

            }

            permissionManager.onRequestCompleted()
            finish()
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}