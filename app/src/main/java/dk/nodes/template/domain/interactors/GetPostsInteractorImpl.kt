package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.executor.Executor
import dk.nodes.template.domain.interactors.base.BaseInteractor
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by bison on 24-06-2017.
 */
class GetPostsInteractorImpl(executor: Executor, val postRepository: PostRepository) : BaseInteractor(executor), GetPostsInteractor {
    override var output : GetPostsInteractor.Output? = null
    override var input : GetPostsInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        input?.let {
            // do something with unwrapped input
        }
        try {
            val posts = postRepository.getPosts()
            runOnUIThread {
                output?.onPostsLoaded(posts)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onError(e.message ?: "Unknown error")
            }
        }
    }
}