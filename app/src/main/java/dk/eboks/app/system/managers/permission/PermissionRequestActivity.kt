package dk.eboks.app.system.managers.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import dk.eboks.app.App
import dk.eboks.app.R
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_permission_request.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 22/02/18.
 */
class PermissionRequestActivity : BaseActivity() {
    @Inject lateinit var permissionManager: PermissionManager
    private val PERMISSION_REQUEST = 4156

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_request)
        //App.instance().appComponent.inject(this)
        component.inject(this)
        requestThemPermissions()
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

        ActivityCompat.requestPermissions(this, perm_array, PERMISSION_REQUEST)
        /*
        if (ContextCompat.checkSelfPermission(this@PermissionRequestActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this@PermissionRequestActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this@PermissionRequestActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST)

        }
        else
            Timber.e("Permission already granted")
            */
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_REQUEST) {
            Timber.e("Permission request completed")
            for (i in 0..permissions.size - 1) {
                Timber.e("Requested perm ${permissions[i]} = ${grantResults[i] == PackageManager.PERMISSION_GRANTED}")
                permissionManager.permsToCheck?.let { perms ->
                    perms[i].wasGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED
                }

            }

            permissionManager.permsToCheck?.forEach { perm ->
                if(ContextCompat.checkSelfPermission(this@PermissionRequestActivity, perm.perm) == PackageManager.PERMISSION_GRANTED)
                {
                    perm.wasGranted = true
                }

            }

            permissionManager.onRequestCompleted()
            finish()
        }
    }
}