package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.ThreadExecutor
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 26/07/17.
 */
@Module
class ExecutorModule {
    @Provides
    @AppScope
    fun provideExecutor() : Executor
    {
        return ThreadExecutor()
    }
}