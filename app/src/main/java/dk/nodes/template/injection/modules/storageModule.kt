package dk.nodes.template.injection.modules

import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.storage.PrefManagerImpl
import org.koin.dsl.module

val storageModule = module {
    single<PrefManager> { PrefManagerImpl(get()) }
}