package dk.nodes.template.domain.interactors

import dk.nodes.arch.domain.interactor.Interactor
import dk.nodes.template.domain.models.Post

interface GetPostsInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val i : Int)

    interface Output {
        fun onPostsLoaded(posts: List<Post>)
        fun onError(msg : String)
    }
}