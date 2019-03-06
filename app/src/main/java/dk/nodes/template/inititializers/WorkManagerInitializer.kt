package dk.nodes.template.inititializers

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import javax.inject.Inject

class WorkManagerInitializer @Inject constructor(private val workerFactory: WorkerFactory) :
    AppInitializer {
    override fun init(app: Application) {
        WorkManager.initialize(app, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }
}