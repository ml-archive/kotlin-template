package dk.eboks.app.domain.interactors

import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class GetPostsInteractorImpl(executor: Executor, val postRepository: dk.eboks.app.domain.repositories.PostRepository) : BaseInteractor(executor), dk.eboks.app.domain.interactors.GetPostsInteractor {
    override var output : dk.eboks.app.domain.interactors.GetPostsInteractor.Output? = null
    override var input : dk.eboks.app.domain.interactors.GetPostsInteractor.Input? = null

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
        } catch (e: dk.eboks.app.domain.repositories.RepositoryException) {
            runOnUIThread {
                output?.onError(e.message ?: "Unknown error")
            }
        }
    }
}