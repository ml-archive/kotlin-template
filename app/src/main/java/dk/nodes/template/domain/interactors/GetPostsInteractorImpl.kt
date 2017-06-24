package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.interactors.base.BaseInteractor
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by bison on 24-06-2017.
 */
class GetPostsInteractorImpl(val callback: GetPostsInteractor.Callback,
                             val postRepository: PostRepository) : BaseInteractor(), GetPostsInteractor {
    override fun execute() {
        try {
            val posts = postRepository.getPosts()
            launch(UI)
            {
                callback.onPostsLoaded(posts)
            }
        } catch (e: RepositoryException) {
            launch(UI)
            {
                callback.onError(e.message ?: "Unknown error")
            }
        }
    }

}