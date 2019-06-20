package dk.nodes.template.injection.modules

import dk.nodes.template.network.RestPostRepository
import dk.nodes.template.repositories.PostRepository
import org.koin.dsl.module

val restRepositoryModule = module {
    single<PostRepository> { RestPostRepository(get()) }
}