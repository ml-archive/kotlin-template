package dk.nodes.template.injection.modules

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.ThreadExecutor
import dk.nodes.arch.domain.injection.scopes.AppScope
import javax.inject.Singleton

@Module
class ExecutorModule {
    @Provides
    @Singleton
    fun provideExecutor(): Executor {
        return ThreadExecutor()
    }
}