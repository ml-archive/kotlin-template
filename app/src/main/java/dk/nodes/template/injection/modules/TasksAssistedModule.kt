package dk.nodes.template.injection.modules

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@Module(includes = [AssistedInject_TasksAssistedModule::class])
@AssistedModule
interface TasksAssistedModule