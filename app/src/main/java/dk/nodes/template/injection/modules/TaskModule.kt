package dk.nodes.template.injection.modules

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope

@Module(includes = [WorkerBindingModule::class, TasksAssistedModule::class])
class TasksModule {
    @Provides
    @AppScope
    fun provideWorkManager(): WorkManager = WorkManager.getInstance()
}