package dk.nodes.template.domain.interactors

import dk.nodes.arch.domain.interactor.ResultInteractor
import dk.nodes.template.domain.models.Post

interface GetPostsInteractor : ResultInteractor<GetPostsInteractor.Input, List<Post>> {
    data class Input(val i: Int)
}