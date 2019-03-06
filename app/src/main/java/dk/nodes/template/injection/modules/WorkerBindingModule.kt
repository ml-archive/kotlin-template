package dk.nodes.template.injection.modules

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.domain.tasks.SampleTask
import dk.nodes.template.injection.annotations.WorkerKey
import dk.nodes.template.injection.factories.ChildWorkerFactory
import dk.nodes.template.injection.factories.DaggerWorkerFactory

@Module
interface WorkerBindingModule {

    @AppScope
    @Binds
    fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SampleTask::class)
    fun bindSampleTask(factory: SampleTask.Factory): ChildWorkerFactory
}