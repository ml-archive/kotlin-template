package dk.eboks.app.initializers

import android.app.Application
import android.os.Build
import dk.nodes.locksmith.core.Locksmith
import dk.nodes.locksmith.core.models.LocksmithConfiguration
import javax.inject.Inject

class LocksmithInitializer @Inject constructor() : AppInitializer {
    override fun init(app: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Locksmith.init(app, LocksmithConfiguration(120))
//            Locksmith.instance.initFingerprint()
//            Locksmith.Builder(this)
//                    .setKeyValidityDuration(120)
//                    .setUseFingerprint(true)
//                    .build()
        } else {
            Locksmith.init(app, LocksmithConfiguration(120))
//            Locksmith.Builder(this)
//                    .build()
//                    .init()
        }
    }
}