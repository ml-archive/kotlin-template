package dk.eboks.app.domain.interactors

import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface GetPostsInteractor : Interactor
{
    var input : dk.eboks.app.domain.interactors.GetPostsInteractor.Input?
    var output : dk.eboks.app.domain.interactors.GetPostsInteractor.Output?

    data class Input(val i : Int)

    interface Output {
        fun onPostsLoaded(posts: List<dk.eboks.app.domain.models.Post>)
        fun onError(msg : String)
    }
}