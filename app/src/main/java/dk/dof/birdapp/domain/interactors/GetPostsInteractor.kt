package dk.dof.birdapp.domain.interactors

import dk.dof.birdapp.domain.models.Post
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
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