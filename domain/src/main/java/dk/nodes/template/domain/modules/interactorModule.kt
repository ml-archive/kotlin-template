package dk.nodes.template.domain.modules

import dk.nodes.template.domain.interactors.PostsInteractor
import org.koin.dsl.module

val interactorModule = module {
    factory { PostsInteractor(get()) }
}