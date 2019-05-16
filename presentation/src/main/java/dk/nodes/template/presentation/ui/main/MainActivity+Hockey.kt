package dk.nodes.template.presentation.ui.main

import dk.nodes.template.presentation.BuildConfig
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.CrashManagerListener
import net.hockeyapp.android.UpdateManager

fun MainActivity.setupHockey() {
    if (BuildConfig.DEBUG) {
        // Auto-send crashes without asking user
        CrashManager.register(this, object : CrashManagerListener() {
            override fun shouldAutoUploadCrashes(): Boolean {
                return true
            }
        })

        // Check for updates from Hockey
        UpdateManager.register(this)
    }

    // GDPR / Google's Personal/Sensitive policy dictates that we should ask the user
    // in user facing builds
    else {
        CrashManager.register(this)
    }
}