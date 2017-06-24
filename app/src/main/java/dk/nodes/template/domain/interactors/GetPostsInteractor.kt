package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.interactors.base.Interactor
import dk.nodes.template.domain.models.Post

/**
 * Created by bison on 24-06-2017.
 */
interface GetPostsInteractor : Interactor
{
    interface Callback {
        fun onPostsLoaded(posts: List<Post>)
        fun onError(msg : String)
    }
}