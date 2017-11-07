package dk.nodes.template.presentation.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dk.nodes.template.BuildConfig
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager

/**
 * Created by bison on 01/11/17.
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForUpdates()
    }

    override fun onResume() {
        super.onResume()
        CrashManager.register(this)
    }

    public override fun onPause() {
        super.onPause()
        unregisterManagers()
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterManagers()
    }

    private fun checkForUpdates() {
        if(BuildConfig.DEBUG && BuildConfig.FLAVOR == "staging") {
            UpdateManager.register(this)
        }
    }

    private fun unregisterManagers() {
        if(BuildConfig.DEBUG && BuildConfig.FLAVOR == "staging")
            UpdateManager.unregister()
    }
}