package dk.nodes.template.domain.executor.injection

import dagger.Module
import dagger.Provides
import dk.nodes.template.domain.executor.Executor
import dk.nodes.template.domain.executor.ThreadExecutor
import dk.nodes.template.injection.ApplicationScope

/**
 * Created by bison on 26/07/17.
 */
@Module
class ExecutorModule {
    @Provides
    @ApplicationScope
    fun provideExecutor() : Executor
    {
        return ThreadExecutor()
    }
}